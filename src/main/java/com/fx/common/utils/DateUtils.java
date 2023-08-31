package com.fx.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

    private final static String YYYYMMDD = "yyyyMMdd";

    public static final ThreadLocal<SimpleDateFormat> FORMATTER_YYYYMMDD = new ThreadLocal<>()    {
        @Override
        protected SimpleDateFormat initialValue()   {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYYMMDD);
            return simpleDateFormat;
        }
    };

    public static final String getCurrentDate()    {
        return FORMATTER_YYYYMMDD.get().format(Calendar.getInstance().getTime());
    }

    public static final String getPreviousDate()    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, Calendar.getInstance().get(Calendar.DATE) - 1);
        return FORMATTER_YYYYMMDD.get().format(calendar.getTime());
    }

    public static final String getNextDate()    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, Calendar.getInstance().get(Calendar.DATE) + 1);
        return FORMATTER_YYYYMMDD.get().format(calendar.getTime());
    }

    public static void main(String[] args) {
        System.out.println(getPreviousDate());
        System.out.println(getCurrentDate());
        System.out.println(getNextDate());
    }
}
