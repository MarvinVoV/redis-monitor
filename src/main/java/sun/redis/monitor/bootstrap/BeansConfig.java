package sun.redis.monitor.bootstrap;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * Created by yamorn on 2015/10/30.
 * <p/>
 * Beans configuration
 */
@Configuration
@ComponentScan(
        basePackages = {"sun.redis.monitor"},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = Controller.class
        ))
@PropertySource({
        "classpath:redis.properties",
        "classpath:jdbc.properties",
        "classpath:redis-monitor.properties"
})
public class BeansConfig {
    @Resource
    Environment env;

    /**
     * Bean only required for @Value("{}") annotations.
     * Remove this bean if you are not using @Value annotations for injecting properties
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Jedis
     */
    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(env.getProperty("redis.hostName"));
        factory.setPort(Integer.valueOf(env.getProperty("redis.port")));
        factory.setTimeout(Integer.valueOf(env.getProperty("redis.timeout")));
        factory.setUsePool(true);
        factory.setPoolConfig(jedisPoolConfig());
        return factory;
    }

    /**
     * Jedis Pool
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(Integer.valueOf(env.getProperty("redis.minIdle")));
        config.setMaxIdle(Integer.valueOf(env.getProperty("redis.maxIdle")));
        config.setMaxTotal(Integer.valueOf(env.getProperty("redis.maxTotal")));
        config.setMaxWaitMillis(Long.valueOf(env.getProperty("redis.maxWaitMillis")));
        config.setTestOnBorrow(Boolean.valueOf(env.getProperty("redis.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(env.getProperty("redis.testOnReturn")));
        config.setTestWhileIdle(Boolean.valueOf(env.getProperty("redis.testWhileIdle")));
        config.setNumTestsPerEvictionRun(Integer.valueOf(env.getProperty("redis.numTestsPerEvictionRun")));
        config.setTimeBetweenEvictionRunsMillis(Integer.valueOf(env.getProperty("redis.timeBetweenEvictionRunsMillis")));
        return config;
    }

    /**
     * RedisTemplate
     */
    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate() {
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<K, V>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    /**
     * JDBC Datasource
     */
    @Bean(destroyMethod = "close")
    public PooledDataSource dataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(env.getProperty("ds.driverClass"));
        dataSource.setJdbcUrl(env.getProperty("ds.url"));
        dataSource.setUser(env.getProperty("ds.username"));
        dataSource.setPassword(env.getProperty("ds.password"));
        dataSource.setAutoCommitOnClose(Boolean.valueOf(env.getProperty("ds.pool.autoCommitOnClose")));
        dataSource.setCheckoutTimeout(Integer.valueOf(env.getProperty("ds.pool.checkoutTimeout")));
        dataSource.setMinPoolSize(Integer.valueOf(env.getProperty("ds.pool.minPoolSize")));
        dataSource.setMaxPoolSize(Integer.valueOf(env.getProperty("ds.pool.maxPoolSize")));
        dataSource.setMaxIdleTime(Integer.valueOf(env.getProperty("ds.pool.maxIdleTime")));
        dataSource.setMaxIdleTimeExcessConnections(Integer.valueOf(env.getProperty("ds.pool.maxIdleTimeExcessConnections")));
        dataSource.setAcquireIncrement(Integer.valueOf(env.getProperty("ds.pool.acquireIncrement")));
        return dataSource;
    }

    /**
     * JdbcTemplate
     */
    @Bean
    public JdbcTemplate jdbcTemplate() throws Exception {
        return new JdbcTemplate(dataSource());
    }


}
