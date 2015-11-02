package sun.redis.monitor.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sun.redis.monitor.domain.TimeSpan;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by root on 2015/11/1.
 */
@RunWith(JUnit4.class)
public class TestDate {
    @Ignore
    @Test
    public void testDate() {
//        Calendar c = Calendar.getInstance();
//        int day = c.get(Calendar.DATE);
//        c.set(Calendar.DATE, day - 2);
//        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
//        System.out.println(str);
        System.out.println(getYesterdayZeroHourTime());
    }

    @Test
    public void testSql() {
        TimeSpan timeSpan = TimeSpan.YESTERDAY;
        String tableName = "redis_monitor";
        String pointLimit = "100";
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
        String sql = "select t.info,t.time from ( select info, time from " + tableName + " " + timeCriteria + " order by time desc " + " limit " + pointLimit + ") " + " as t " + " order by t.time asc";
        System.out.println(sql);
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
