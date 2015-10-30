package sun.redis.monitor.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import sun.redis.monitor.bootstrap.BeansConfig;
import sun.redis.monitor.bootstrap.WebAppInitializer;
import sun.redis.monitor.bootstrap.WebConfig;

/**
 * Created by root on 2015/10/30.
 */
@Configuration
@ComponentScan(basePackages = "sun.redis.monitor",
excludeFilters = {
        @ComponentScan.Filter(
                type= FilterType.ASSIGNABLE_TYPE,
                value = {WebConfig.class, WebAppInitializer.class}
        )
})
@Import(BeansConfig.class)
@ActiveProfiles("integration-test")
public class TestConfig {
}

//http://revelfire.com/spring-webmvc-unit-test-fails-caused-by-java-lang-illegalargumentexception-a-servletcontext-is-required-to-configure-default-servlet-handling/
