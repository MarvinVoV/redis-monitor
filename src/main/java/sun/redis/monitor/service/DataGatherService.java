package sun.redis.monitor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import sun.redis.monitor.domain.JsonEntity;
import sun.redis.monitor.domain.RedisInfoEntity;
import sun.redis.monitor.domain.TimeSpan;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yamorn on 2015/10/30.
 * <p/>
 * Deamon thread for data gather.
 */
@Service
public class DataGatherService implements GatherService<List<JsonEntity>>, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(DataGatherService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String, RedisInfoEntity> redisTemplate;

    private Timer timer;

    @Value("${monitor.rate:1800000}")
    private String rate; // default 30 minutes

    @Value("${monitor.tableName:REDIS_MONITOR}")
    private String tableName;   // default REDIS_MONITOR

    @Value("${monitor.pointLimit:100}")
    private String pointLimit;

    public void destroy() throws Exception {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void afterPropertiesSet() throws Exception {
        timer = new Timer(true);  // set as deamon
        timer.scheduleAtFixedRate(new DataGatherTimerTask(), 1000 * 5, Long.valueOf(rate));
    }

    private class DataGatherTimerTask extends TimerTask {
        ObjectMapper mapper = new ObjectMapper();

        @Override
        public void run() {
            Properties properties = redisTemplate.execute(new RedisCallback<Properties>() {
                public Properties doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.info();
                }
            });
            RedisInfoEntity entity = parse(properties);
            String json = null;
            try {
                json = mapper.writeValueAsString(entity);
            } catch (JsonProcessingException e) {
                logger.error(e.getLocalizedMessage());
            }
            if (json == null) {
                return;
            }
            String sql = "insert into " + tableName + "(info, time) values(?, ?)";
            jdbcTemplate.update(sql, json, new Date());

            // Clear data two days earlier
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DATE);
            c.set(Calendar.DATE, day - 2);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
            String clearSql = "delete from " + tableName + " where time < '" + date + "'";
            jdbcTemplate.update(clearSql);
        }

        RedisInfoEntity parse(Properties properties) {
            RedisInfoEntity entity = new RedisInfoEntity();
            Pattern pattern = Pattern.compile("^db\\d+");
            for (Map.Entry entry : properties.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                String[] fragments = key.split("_");
                String fieldName = "";
                for (int i = 0; i < fragments.length; i++) {
                    if (i > 0) {
                        fieldName += WordUtils.capitalize(fragments[i]);
                    } else {
                        fieldName += fragments[i];
                    }
                }
                if ("".equals(fieldName)) {
                    continue;
                }
                Field field;
                try {
                    field = RedisInfoEntity.class.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    if (type.isAssignableFrom(String.class)) {
                        field.set(entity, value);
                    } else if (type.isAssignableFrom(Long.class) || type == Long.TYPE) {
                        field.setLong(entity, Long.valueOf(value));
                    } else if (type.isAssignableFrom(Integer.class) || type == Integer.TYPE) {
                        field.setInt(entity, Integer.valueOf(value));
                    } else if (type.isAssignableFrom(Boolean.class) || type == Boolean.TYPE) {
                        field.setBoolean(entity, Boolean.valueOf(value));
                    }
                } catch (Exception e) {
                    // empty
                }
                Matcher matcher = pattern.matcher(key);
                if (matcher.find()) {
                    entity.getDbStatistics().add(key + ":" + value);
                }

            }
            return entity;
        }
    }


    public List<JsonEntity> getDataList(TimeSpan timeSpan) {
        String timeCriteria = "";
        switch (timeSpan) {
            case REAL_TIME:
                // empty
                break;
            case ONE_HOUR:
                timeCriteria = " where time > '" + getHourTimeAgo(1) + "' ";
                break;
            case TWO_HOUR:
                timeCriteria = " where time > '" + getHourTimeAgo(2) + "' ";
                break;
            case THREE_HOUR:
                timeCriteria = " where time > '" + getHourTimeAgo(3) + "' ";
                break;
            case FOUR_HOUR:
                timeCriteria = " where time > '" + getHourTimeAgo(4) + "' ";
                break;
            case TODAY:
                timeCriteria = " where time > '" + getZeroHourTime() + "' ";
                break;
            case YESTERDAY:
                timeCriteria = " where time > '" + getYesterdayZeroHourTime() + "' ";
                break;
        }
        String sql = "select t.info,t.time from ( select info, time from " + tableName + " " + timeCriteria
                + " order by time desc " + " limit " + pointLimit + ") " + " as t " + " order by t.time asc";

        final ObjectMapper mapper = new ObjectMapper();
        return jdbcTemplate.query(sql, new RowMapper<JsonEntity>() {
            public JsonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                JsonEntity entity = new JsonEntity();
                entity.setDate(rs.getTimestamp("TIME"));
                RedisInfoEntity info = null;
                try {
                    info = mapper.readValue(rs.getString("INFO"), RedisInfoEntity.class);
                } catch (IOException e) {
                    logger.error(e.getLocalizedMessage());
                }
                if (info != null) {
                    entity.setInfo(info);
                }
                return entity;
            }
        });
    }

    private String getHourTimeAgo(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hours * (-1));
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    private String getZeroHourTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR, -12);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    private String getYesterdayZeroHourTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);
        calendar.set(Calendar.HOUR, -12);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

}