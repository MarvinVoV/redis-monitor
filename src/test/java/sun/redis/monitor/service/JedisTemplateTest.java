package sun.redis.monitor.service;

import org.apache.commons.lang3.text.WordUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.redis.monitor.domain.RedisInfoEntity;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;


/**
 * Created by yamorn on 2015/10/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class JedisTemplateTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedisTemplate() {
        Properties properties = redisTemplate.execute(new RedisCallback<Properties>() {
            public Properties doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.info();
            }
        });
        RedisInfoEntity entity = parse(properties);
        assertNotNull(entity);
        System.out.println(entity);
    }

    private RedisInfoEntity parse(Properties properties) {
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
            if ("db0".equals(key)) {
                System.out.println(value);
            }
            Matcher matcher = pattern.matcher(key);
            if (matcher.find()) {
                entity.getDbStatistics().add(key + ":" + value);
            }

        }
        return entity;
    }
}
