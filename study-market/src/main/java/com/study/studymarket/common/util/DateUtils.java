package com.study.studymarket.common.util;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_YMDH_M_S = "yyyyMMdd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_Y_M_DH_M_S = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String DATETIME_Y_M_DH_M = "yyyy-MM-dd HH:mm";

    /**
     * yyyy-MM-dd
     */
    public static final String DATE_Y_M_D = "yyyy-MM-dd";

    /**
     * yyyy年MM月dd日
     */
    public static final String DATE_Y_M_D_CN = "yyyy年MM月dd日";

    /**
     * yyyy年MM月
     */
    public static final String DATE_Y_M = "yyyy年MM月";

    /**
     * yyyyMMdd
     */
    public static final String DATE_YMD = "yyyyMMdd";

    /**
     * yyyyMMddHHmmss
     */
    public static final String DATETIME_YMDHMS_INFILENAME = "yyyyMMddHHmmss";

    /**
     * yyMMddhhmm
     */
    public static final String DATETIME_YMDHM_INFILENAME = "yyMMddhhmm";

    public static final String DATETIME_UTC_Y_M_DH_M_S = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * /**
     * 返回当前日期加减n天后的日期
     *
     * @param date
     * @param count
     * @return 当前日期加减n天后的日期
     */
    public static Date addHour(Date date, int count) {
        if (date == null) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, count);
        return calendar.getTime();
    }

    /**
     * 返回当前日期加减n秒后的日期
     *
     * @param date
     * @param count
     * @return 当前日期加减n天后的日期
     */
    public static Date addSecond(Date date, int count) {
        if (date == null) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, count);
        return calendar.getTime();
    }

    /**
     * 返回当前日期加减n天后的日期
     *
     * @param date
     * @param count
     * @return 当前日期加减n天后的日期
     */
    public static Date addDay(Date date, int count) {
        if (date == null) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, count);
        return calendar.getTime();
    }

    /**
     * 返回当前日期加减n小时后的时间
     *
     * @param date
     * @param count
     * @return 当前日期加减n小时后的时间
     */
    public static Date addMinute(Date date, int count) {
        if (date == null) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, count);
        return calendar.getTime();
    }

    /**
     * 返回当前日期加减n月后的日期
     *
     * @param date
     * @param count
     * @return 当前日期加减n月后的日期
     */
    public static Date addMonth(Date date, int count) {
        if (date == null) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, count);
        return calendar.getTime();
    }

    /**
     * 返回当前日期加减n月后的日期
     *
     * @param date
     * @param count
     * @return 当前日期加减n月后的日期
     */
    public static Date addYear(Date date, int count) {
        if (date == null) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, count);
        return calendar.getTime();
    }

    //是否是偶数日
    public static boolean isEvenDay(Date date) {
        Calendar ca = Calendar.getInstance();
        int day = ca.get(Calendar.DATE);
        return (day % 2 == 0);
    }

    //单数日
    public static boolean isOddDay(Date date) {
        Calendar ca = Calendar.getInstance();
        int day = ca.get(Calendar.DATE);
        return (day % 2 == 1);
    }

    /**
     * @param date
     * @return
     */
    public static long getHour(Date date) {
        return getField(date, Calendar.HOUR_OF_DAY);
    }

    public static long getField(Date date, int field) {
        Calendar starCal = Calendar.getInstance();
        starCal.setTime(date);
        return starCal.get(field);
    }


    /**
     * 处理日期String得到日期Date
     *
     * @param dateStr
     * @param module
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateStr, String module)
            throws ParseException {
        DateFormat df = new SimpleDateFormat(module);
        return df.parse(dateStr);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param module 使用DateUtils提供的Module
     * @return 返回经过格式化后的日期String
     */
    public static String format(Date date, String module) {
        DateFormat df = new SimpleDateFormat(module);
        if (date != null) {
            return df.format(date);
        } else {
            return StringUtils.EMPTY;
        }
    }

    //由出生日期获得年龄
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 分钟差
     *
     * */
    public static Integer poorMinute(Date newDate, Date oldDate) throws ParseException {
        String fromDate = DateUtils.format(oldDate, DateUtils.DATETIME_Y_M_DH_M_S);
        String toDate = DateUtils.format(newDate, DateUtils.DATETIME_Y_M_DH_M_S);
        long from = new SimpleDateFormat(DateUtils.DATETIME_Y_M_DH_M_S).parse(fromDate).getTime();
        long to = new SimpleDateFormat(DateUtils.DATETIME_Y_M_DH_M_S).parse(toDate).getTime();
        int minutes = (int) ((to - from)/(1000 * 60));
        return minutes;
    }


    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     *
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成指定时间
     *
     *
     * */
    public static Date getNeedTime(int year, int month, int date, int hour, int minute, int second) {
        Calendar cl = Calendar.getInstance();
        cl.set(year, month-1, date);
        cl.set(Calendar.HOUR_OF_DAY, hour);
        cl.set(Calendar.MINUTE, minute);
        cl.set(Calendar.SECOND, second);
        return cl.getTime();
    }

    //判断选择的日期是否是本月
    public static boolean isThisMonth(Date date) {
        long time = date.getTime();
        return isThisTime(time, "yyyy-MM");
    }

    public static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }


    public static String formatDateToUTC(Date date) {
//        try {
//            long localTimeInMillis=date.getTime();
//            /** long时间转换成Calendar */
//            Calendar calendar= Calendar.getInstance();
//            calendar.setTimeInMillis(localTimeInMillis);
//            /** 取得时间偏移量 */
//            int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
//            /** 取得夏令时差 */
//            int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
//            /** 从本地时间里扣除这些差量，即可以取得UTC时间*/
//            calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
//            /** 取得的时间就是UTC标准时间 */
//            Date utcDate=new Date(calendar.getTimeInMillis());
//            return utcDate;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
//        return bjSdf.format(date);
    }

    /**
     * 精确计算相差月数，结果不进位
     *
     *
     * @return
     */
    public static int compareWithMonth(Date startTime, Date endTime) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(startTime);
        LocalDate start = LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DAY_OF_MONTH));
        cl.setTime(endTime);
        LocalDate end = LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DAY_OF_MONTH));

        Period period = Period.between(start, end);
        int month = period.getMonths();
        int year = period.getYears();
        int day = period.getDays();
        System.out.println("year:" + year+" month:" +month + " day:" + day);
        return (year*12) + month;
    }

    public static int compareWithDay(Date startTime, Date endTime) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(startTime);
        LocalDate start = LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DAY_OF_MONTH));
        cl.setTime(endTime);
        LocalDate end = LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DAY_OF_MONTH));

        return (int) (end.toEpochDay() -  start.toEpochDay());
    }

    /**
     * 计算当天属于当月第几周
     * @param dateTime
     *
     * */
    public static int getWeekOfMonth(Date dateTime) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(dateTime);

        LocalDate localDate = LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DAY_OF_MONTH));
        WeekFields weekFields = WeekFields.ISO;
        return localDate.get(weekFields.weekOfMonth());
    }


    public static void main(String[] args)throws Exception {
        System.out.println(formatDateToUTC(new Date()));

    }
}
