package com.example.rhong.musictest.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rhong on 2017/7/6.
 */

public class DateFormatUtil {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
    public static Date date = new Date();

    public static String getDate(long mills) {
        date.setTime(mills);
        return simpleDateFormat.format(date);
    }
}
