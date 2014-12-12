package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeHelper {
    private static String getTime() {
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }
}
