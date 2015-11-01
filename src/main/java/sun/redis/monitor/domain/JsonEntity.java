package sun.redis.monitor.domain;

import java.util.Date;

/**
 * Created by yamorn on 2015/10/31.
 */
public class JsonEntity {
    private RedisInfoEntity info;
    private Date date;

    public JsonEntity() {
    }

    public JsonEntity(RedisInfoEntity info, Date date) {
        this.info = info;
        this.date = date;
    }

    public RedisInfoEntity getInfo() {
        return info;
    }

    public void setInfo(RedisInfoEntity info) {
        this.info = info;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
