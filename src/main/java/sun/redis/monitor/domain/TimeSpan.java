package sun.redis.monitor.domain;

/**
 * Created by root on 2015/11/2.
 */
public enum TimeSpan {
    REAL_TIME,
    ONE_HOUR,
    TWO_HOUR,
    THREE_HOUR,
    FOUR_HOUR,
    TODAY,
    YESTERDAY;

    @Override
    public String toString() {
        String str;
        switch (this){
            case REAL_TIME:
                str = "REAL_TIME";
                break;
            case ONE_HOUR:
                str = "ONE_HOUR";
                break;
            case TWO_HOUR:
                str = "TWO_HOUR";
                break;
            case THREE_HOUR:
                str = "THREE_HOUR";
                break;
            case FOUR_HOUR:
                str = "FOUR_HOUR";
                break;
            case TODAY:
                str = "TODAY";
                break;
            case YESTERDAY:
                str = "YESTERDAY";
                break;
            default:
                str = "REAL_TIME";
        }

        return str;
    }
}
