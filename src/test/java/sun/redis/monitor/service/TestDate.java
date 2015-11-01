package sun.redis.monitor.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by root on 2015/11/1.
 */
@RunWith(JUnit4.class)
public class TestDate {
    @Test
    public void testDate(){
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 2);
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
        System.out.println(str);

    }
}
