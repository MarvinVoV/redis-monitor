package sun.redis.monitor.service;

import sun.redis.monitor.domain.TimeSpan;

/**
 * Created by yamorn on 2015/10/31.
 */
public interface GatherService<T> {
    T getDataList(TimeSpan timeSpan);
}
