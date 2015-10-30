package sun.redis.monitor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.redis.monitor.domain.RedisInfoEntity;

import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yamorn on 2015/10/30.
 * <p/>
 * Deamon thread for data gather.
 */
@Service
public class DataGatherService implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(DataGatherService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String, RedisInfoEntity> redisTemplate;

    private Timer timer;

    @Value("monitor.rate")
    private long rate; // milliseconds

    @Value("monitor.tableName")
    private String tableName;

    public void destroy() throws Exception {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void afterPropertiesSet() throws Exception {
        timer = new Timer(true);  // set as deamon
        timer.scheduleAtFixedRate(new DataGatherTimerTask(), 0, rate);
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
            if (StringUtils.isEmpty(tableName)) {
                tableName = "REDIS_MONITOR";
            }
            String sql = "INSERT INTO " + tableName + "(INFO, TIME) VALUES(?, ?)";
            jdbcTemplate.update(sql, json, new Date());
        }

        RedisInfoEntity parse(Properties properties) {
            RedisInfoEntity entity = new RedisInfoEntity();
            //
            return entity;
        }
    }


}
