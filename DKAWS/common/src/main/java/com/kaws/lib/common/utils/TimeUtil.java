package com.kaws.lib.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    /**
     * 准备第一个模板，从字符串中提取出日期数字
     */
    private static String pat1 = "yyyy-MM-dd HH:mm:ss";
    /**
     * 准备第二个模板，将提取后的日期数字变为指定的格式
     */
    private static String pat2 = "yyyy年MM月dd日 HH:mm:ss";
    /**
     * 实例化模板对象
     */
    private static SimpleDateFormat sdf1 = new SimpleDateFormat(pat1);
    private static SimpleDateFormat sdf2 = new SimpleDateFormat(pat2);
    private static long timeMilliseconds;

    public static Long farmatTime(String string) {
        Date date = null;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = Date(sf.parse(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static Date Date(Date date) {
        Date datetimeDate;
        datetimeDate = new Date(date.getTime());
        return datetimeDate;
    }

    public static Date Dates() {
        Date datetimeDate;
        Long dates = 1361515285070L;
        datetimeDate = new Date(dates);
        return datetimeDate;
    }


    public static Date Date() {
        Date datetimeDate;
        Long dates = 1361514787384L;
        datetimeDate = new Date(dates);
        return datetimeDate;
    }

    /**
     * 如果在1分钟之内发布的显示"刚刚" 如果在1个小时之内发布的显示"XX分钟之前" 如果在1天之内发布的显示"XX小时之前"
     * 如果在今年的1天之外的只显示“月-日”，例如“05-03” 如果不是今年的显示“年-月-日”，例如“2014-03-11”
     *
     * @param time
     * @return
     */
    public static String translateTime(String time) {
        // 在主页面中设置当天时间
        Date nowTime = new Date();
        String currDate = sdf1.format(nowTime);
        long currentMilliseconds = nowTime.getTime();// 当前日期的毫秒值
        Date date = null;
        try {
            // 将给定的字符串中的日期提取出来
            date = sdf1.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
            return time;
        }
        if (date != null) {
            timeMilliseconds = date.getTime();
        }

        long timeDifferent = currentMilliseconds - timeMilliseconds;
        if (timeDifferent < 60000) {// 一分钟之内

            return "刚刚";
        }
        if (timeDifferent < 3600000) {// 一小时之内
            long longMinute = timeDifferent / 60000;
            int minute = (int) (longMinute % 100);
            return minute + "分钟之前";
        }
        long l = 24 * 60 * 60 * 1000; // 每天的毫秒数
        if (timeDifferent < l) {// 小于一天
            long longHour = timeDifferent / 3600000;
            int hour = (int) (longHour % 100);
            return hour + "小时之前";
        }
        if (timeDifferent >= l) {
            String currYear = currDate.substring(0, 4);
            String year = time.substring(0, 4);
            if (!year.equals(currYear)) {
                return time.substring(0, 10);
            }
            return time.substring(5, 10);
        }
        return time;
    }


    public static String timeFormat(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = null;
        try {
            // 将给定的字符串中的日期提取出来
            date = sdf.parse(time);
        } catch (Exception e) {
            DebugUtil.debug("--时间解析-->", "错误");
            return time;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf1.format(date);
    }

    /**
     * 是否小于4个小时
     *
     * @param time
     * @return
     */
    public static boolean less4hours(String time) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        // 在主页面中设置当天时间
        Date nowTime = new Date();
        long currentMilliseconds = nowTime.getTime();// 当前日期的毫秒值
        Date date = null;
        try {
            // 将给定的字符串中的日期提取出来
            date = sdf1.parse(time);
        } catch (Exception e) {
            DebugUtil.debug("--时间解析-->", "错误");
            return false;
        }
        if (date != null) {
            timeMilliseconds = date.getTime();
        }

        long timeDifferent = currentMilliseconds - timeMilliseconds;
        long l = 60 * 60 * 1000 * 4; // 4小时的毫秒数

        if (Math.abs(timeDifferent) < l) {
            return true;
        }
        return false;
    }

    /**
     * 如果在1分钟之内发布的显示"刚刚" 如果在1个小时之内发布的显示"XX分钟之前" 如果在1天之内发布的显示"XX小时之前"
     * 如果在今年的1天之外的只显示“月-日”，例如“05-03” 如果不是今年的显示“年-月-日”，例如“2014-03-11”
     *
     * @param time 适用于iso8601的时间格式
     * @return
     */
    public static String getTranslateTime(String time) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        // 在主页面中设置当天时间
        Date nowTime = new Date();
        String currDate = sdf1.format(nowTime);
        long currentMilliseconds = nowTime.getTime();// 当前日期的毫秒值
        Date date = null;
        try {
            // 将给定的字符串中的日期提取出来
            date = sdf1.parse(time);
        } catch (Exception e) {
            DebugUtil.debug("--时间解析-->", "错误");
            return time;
        }
        if (date != null) {
            timeMilliseconds = date.getTime();
        }

        long timeDifferent = currentMilliseconds - timeMilliseconds;
        if (timeDifferent < 60000) {// 一分钟之内

            return "刚刚";
        }
        if (timeDifferent < 3600000) {// 一小时之内
            long longMinute = timeDifferent / 60000;
            int minute = (int) (longMinute % 100);
            return minute + "分钟之前";
        }
        long l = 24 * 60 * 60 * 1000; // 每天的毫秒数
        if (timeDifferent < l) {// 小于一天
            long longHour = timeDifferent / 3600000;
            int hour = (int) (longHour % 100);
            return hour + "小时之前";
        }
        if (timeDifferent >= l) {
            String currYear = currDate.substring(0, 4);
            String year = time.substring(0, 4);
            if (!year.equals(currYear)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return dateFormat.format(date);
            }
            return time.substring(5, 10);
        }
        return time;
    }


    /**
     * 如果在1分钟之内发布的显示"一分钟后" 如果在1个小时之内发布的显示"XX分钟后" 如果在1天之内发布的显示"XX小时后"
     * 如果在今年的1天之外的只显示“月-日”，例如“05-03” 如果不是今年的显示“年-月-日”，例如“2014-03-11”
     *
     * @param time 适用于iso8601的时间格式
     * @return
     */
    public static String getTranslateTimeLater(String time) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        // 在主页面中设置当天时间
        Date nowTime = new Date();
        String currDate = sdf1.format(nowTime);
        long currentMilliseconds = nowTime.getTime();// 当前日期的毫秒值
        Date date = null;
        try {
            // 将给定的字符串中的日期提取出来
            date = sdf1.parse(time);
        } catch (Exception e) {
            DebugUtil.debug("--时间解析-->", "错误");
            return time;
        }
        if (date != null) {
            timeMilliseconds = date.getTime();
        }


        long timeDifferent = currentMilliseconds - timeMilliseconds;
        if (Math.abs(timeDifferent) < 60000) {// 一分钟之内

            return "1分后";
        }
        if (Math.abs(timeDifferent) < 3600000) {// 一小时之内
            long longMinute = timeDifferent / 60000;
            int minute = (int) (longMinute % 100);
            return Math.abs(minute) + "分钟后";
        }
        long l = 24 * 60 * 60 * 1000; // 每天的毫秒数
        if (Math.abs(timeDifferent) < l) {// 小于一天
            long longHour = timeDifferent / 3600000;
            int hour = (int) (longHour % 100);
            return Math.abs(hour) + "小时后";
        }
        if (Math.abs(timeDifferent) >= l) {
            String currYear = currDate.substring(0, 4);
            String year = time.substring(0, 4);
            if (!year.equals(currYear)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return dateFormat.format(date);
            }
            return time.substring(5, 10);
        }
        return time;
    }


    /**
     * 是否小于1小时
     *
     * @param time
     * @return
     */
    public static boolean lessOneHour(String time) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        // 在主页面中设置当天时间
        Date nowTime = new Date();
        long currentMilliseconds = nowTime.getTime();// 当前日期的毫秒值
        Date date = null;
        try {
            // 将给定的字符串中的日期提取出来
            date = sdf1.parse(time);
        } catch (Exception e) {
            DebugUtil.debug("--时间解析-->", "错误");
            return false;
        }
        if (date != null) {
            timeMilliseconds = date.getTime();
        }

        long timeDifferent = currentMilliseconds - timeMilliseconds;
        long l = 60 * 60 * 1000; // 1小时的毫秒数
        if (Math.abs(timeDifferent) < l) {
            return true;
        }
        return false;
    }


    /**
     * 当天显示  时：分  当年显示 月 日： 时  ： 分  去年 显示  年月日时分
     *
     * @param time 适用于iso8601的时间格式
     * @return
     */
    public static String getTime(String time) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        // 在主页面中设置当天时间
        Date nowTime = new Date();
        String currDate = sdf1.format(nowTime);
        long currentMilliseconds = nowTime.getTime();// 当前日期的毫秒值
        Date date = null;
        try {
            // 将给定的字符串中的日期提取出来
            date = sdf1.parse(time);
        } catch (Exception e) {
            DebugUtil.debug("--时间解析-->", "错误");
            return time;
        }

        if (isToday(sdf.format(date))) {
            return sdf2.format(date);
        }

        String currYear = currDate.substring(0, 4);
        String year = time.substring(0, 4);
        if (!year.equals(currYear)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        }
        return time.substring(5, 10);
    }


    /**
     * 获取当前日期
     */
    public static String getData() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    /**
     * 比较日期与当前日期的大小
     */
    public static boolean isToday(String s1) {
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到指定模范的时间
        Date d1 = null;
        try {
            d1 = sdf.parse(s1);
        } catch (ParseException e) {
            return false;
        }
        Date d2 = null;
        try {
            d2 = sdf.parse(getData());
        } catch (ParseException e) {
            return false;
        }
        //比较
        if (((d1.getTime() - d2.getTime()) / (24 * 3600 * 1000)) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean DateCompare(String data1, String data2) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到指定模范的时间
        Date d1 = sdf.parse(data1);
        Date d2 = sdf.parse(data2);
        //比较
        if (((d1.getTime() - d2.getTime()) / (24 * 3600 * 1000)) >= 1) {
            return true;
        } else {
            return false;
        }
    }
}
