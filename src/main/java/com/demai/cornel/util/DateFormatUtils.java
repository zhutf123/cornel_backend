/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Create By zhutf 19-10-6 下午3:44
 */
public final class DateFormatUtils {

    /**
     * 默认格式化格式。与ISO8601不一致，无T
     */
    public static final String ISO_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String ISO_DATETIME_NOSEC_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String ISO_DATETIME_NOSPLIT_PATTERN = "yyyyMMddHHmmss";
    public static final String ISO_DATE_PATTERN = "yyyy-MM-dd";
    public static final String ISO_DATE_SLASH_PATTERN = "yyyy/MM/dd";
    public static final String ISO_TIME_PATTERN = "HH:mm:ss";
    public static final String ISO_HM_PATTERN = "HH:mm";
    public static final String ISO_EXPIRES_PATTERN = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";

    private DateFormatUtils() {

    }

    /**
     * 输出格式为@ISO_DATE_PATTERN
     *
     * @param dt 时间
     * @return
     */
    public static String format(Date dt) {
        return format(dt, ISO_DATE_PATTERN);
    }

    public static String format(Calendar calendar) {
        return format(calendar, ISO_DATE_PATTERN);
    }

    public static String format(long millis) {
        return format(millis, ISO_DATE_PATTERN);
    }

    /**
     * 输出格式为@ISO_DATETIME_PATTERN
     *
     * @param dt
     * @return
     */
    public static String formatDateTime(Date dt) {
        return format(dt, ISO_DATETIME_PATTERN);
    }

    public static String formatDateTime(Calendar calendar) {
        return format(calendar, ISO_DATETIME_PATTERN);
    }

    public static String formatDateTime(long millis) {
        return format(millis, ISO_DATETIME_PATTERN);
    }

    public static String formatDateTime(LocalDateTime localDateTime) {
        return localDateTime.toString(ISO_DATETIME_PATTERN);
    }

    public static String format(Date dt, String pattern) {
        return format(dt.getTime(), pattern);
    }

    public static String format(Calendar calendar, String pattern) {

        return format(calendar.getTimeInMillis(), pattern);
    }

    public static String format(long millis, String pattern) {
        // DateTimeFormat有cache,可以不用默认生成实例对象
        return DateTimeFormat.forPattern(pattern).print(millis);
    }

    public static String format(long millis, String pattern, Locale locale) {
        // DateTimeFormat有cache,可以不用默认生成实例对象
        return DateTimeFormat.forPattern(pattern).withLocale(locale).print(millis);
    }

    public static String format(Date dt, String pattern, Locale locale) {
        return format(dt.getTime(), pattern, locale);
    }

    /**
     * 不在使用jodaTime包
     */
    @Deprecated
    public static String formatLocalDate(DateTime dateTime, String pattern) {
        return formatLocalDate(dateTime, dateTime.getZone(), pattern);
    }

    /**
     * 不在使用jodaTime包
     */
    @Deprecated
    public static String formatLocalDate(DateTime dateTime, DateTimeZone zone, String pattern) {
        return dateTime.toLocalDate().toString(DateTimeFormat.forPattern(pattern).withZone(zone));
    }

    /**
     * 不在使用jodaTime包
     */
    @Deprecated
    public static String formatLocalDateTime(DateTime dateTime, String pattern) {
        return formatLocalDateTime(dateTime, dateTime.getZone(), pattern);
    }

    /**
     * 不在使用jodaTime包
     */
    @Deprecated
    public static String formatLocalDateTime(DateTime dateTime, DateTimeZone zone, String pattern) {
        return dateTime.toLocalDateTime().toString(DateTimeFormat.forPattern(pattern).withZone(zone));
    }

    /**
     * 不在使用jodaTime包
     */
    @Deprecated
    public static String format(DateTime dateTime, String pattern) {
        return format(dateTime, dateTime.getZone(), pattern);
    }

    /**
     * 不在使用jodaTime包
     */
    @Deprecated
    public static String format(DateTime dateTime, DateTimeZone zone, String pattern) {
        return dateTime.toString(DateTimeFormat.forPattern(pattern).withZone(zone));
    }

    /**
     * 不在使用jodaTime包
     */
    @Deprecated
    public static String format(DateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.toString(formatter);
    }

    /**
     * 解析字符串为日期格式，格式@ISO_DATE_PATTERN
     *
     * @param dateStr
     * @return
     */
    public static Date parse(String dateStr) {
        return parse(dateStr, ISO_DATE_PATTERN);
    }

    public static Date parseDateTime(String dateStr) {
        return parse(dateStr, ISO_DATETIME_PATTERN);
    }

    public static Date parse(String dateStr, String pattern) {
        // 使用withZoneUTC防止夏令时问题
        return LocalDateTime.parse(dateStr, DateTimeFormat.forPattern(pattern).withZoneUTC()).toDate();
    }

    public static String getAfterTime(long time,String timeFormat,Integer afterDays){
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat(timeFormat);
        calendar1.add(Calendar.DATE, afterDays);
        return sdf1.format(calendar1.getTime());
    }
}
