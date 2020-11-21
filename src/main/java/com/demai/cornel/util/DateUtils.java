/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

/**
 * Create By zhutf  19-10-6  下午3:43
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final Date MAX_DATE = new Date(Long.MAX_VALUE);
    public static final Date ZERO_DATE = new Date(0);
    public static final Date MIN_DATE = new Date(Long.MIN_VALUE);

    //0001-01-01T00:00:00Z  公元1年
    public static final Date AD_ONE_DATE = new Date(-62135596800000L);
    public static final DateTime AD_ONE_DATETIME = new DateTime(-62135596800000L, DateTimeZone.UTC);

    public static final DateTimeZone TZ_CST = DateTimeZone.forID("+0800");
    public static final long TZ_CST_MS = 28800000;

    public static boolean isToday(Date date) {
        return isSameDay(date, now());
    }

    /**
     * 返回大的时间，空值比非空值小
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null)
            return null;
        if (d1 == null)
            return d2;
        if (d2 == null)
            return d1;
        return (d1.after(d2)) ? d1 : d2;
    }

    /**
     * 返回小的时间，空值比非空值小
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null)
            return null;
        if (d1 == null)
            return d2;
        if (d2 == null)
            return d1;
        return (d1.before(d2)) ? d1 : d2;
    }

    /**
     * 获取时间对于的seconds值
     *
     * @param d
     * @return
     */
    public static int getSecondTime(Date d) {
        return new Long(d.getTime() / MILLIS_PER_SECOND).intValue();
    }

    /**
     * 清除时间
     *
     * @param date
     * @return
     */
    public static Date clearTime(Date date) {
        return truncate(date, Calendar.DAY_OF_MONTH);
    }

    public static LocalDateTime clearTime(LocalDateTime localDateTime) {
        return getStart(localDateTime);
    }

    public static LocalDateTime getStart(LocalDateTime localDateTime) {
        return getStart(localDateTime.toLocalDate());
    }

    public static LocalDateTime getStart(LocalDate localDate) {
        return localDate.atTime(LocalTime.MIN);
    }

    /**
     * 返回一天的最大值
     */
    public static Date getEnd(Date date) {
        return getEnd(date, Calendar.DATE);
    }

    public static LocalDateTime getEnd(LocalDateTime localDateTime) {
        return getEnd(localDateTime.toLocalDate());
    }

    public static LocalDateTime getEnd(LocalDate localDate) {
        return localDate.atTime(LocalTime.MAX);
    }

    public static Date getEnd(Date date, int field) {
        Preconditions.checkNotNull(date);

        switch (field) {
            case YEAR:
                return addMilliseconds(addYears(truncate(date, field), 1), -1);
            case MONTH:
                return addMilliseconds(addMonths(truncate(date, field), 1), -1);
            case DATE:
                return addMilliseconds(addDays(truncate(date, field), 1), -1);
            case HOUR:
                return addMilliseconds(addHours(truncate(date, field), 1), -1);
            case MINUTE:
                return addMilliseconds(addMinutes(truncate(date, field), 1), -1);
            case SECOND:
                return addMilliseconds(addSeconds(truncate(date, field), 1), -1);
            case MILLISECOND:
                return date;
        }
        throw new IllegalArgumentException("field:" + field + " not supported!");
    }

    public static Date toDate(int year, int monthOfYear, int dayOfMonth) {
        return toDate(year, monthOfYear, dayOfMonth, 0);
    }

    public static Date toDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay) {
        return toDate(year, monthOfYear, dayOfMonth, hourOfDay, 0);
    }

    public static Date toDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        return toDate(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 0);
    }

    public static Date toDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour,
                              int secondOfMinute) {
        return toDate(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, 0);
    }

    /**
     * 根据年月日生成日期对象
     *
     * @param year           the year
     * @param monthOfYear    the month of the year, from 1 to 12
     * @param dayOfMonth     the day of the month, from 1 to 31
     * @param hourOfDay      the hour of the day, from 0 to 23
     * @param minuteOfHour   the minute of the hour, from 0 to 59
     * @param secondOfMinute the second of the minute, from 0 to 59
     * @return
     */
    public static Date toDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond) {

        /*
        * https://github.com/JodaOrg/joda-time/issues/334
        * Joda-Time uses the data from the IANA time-zone database without alteration. The JDK applies a cut-off for older dates.
        * This means that for older dates, the two systems give different answers.
        * 例如1900-01-01通过LocalDateTime转为Date会出现问题
        */
        //toDate(LocalDateTime.of(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
        Calendar calendar = Calendar.getInstance();
        calendar.set(YEAR, year);
        calendar.set(MONTH, monthOfYear - 1);
        calendar.set(DATE, dayOfMonth);
        calendar.set(HOUR_OF_DAY, hourOfDay);
        calendar.set(MINUTE, minuteOfHour);
        calendar.set(SECOND, secondOfMinute);
        calendar.set(MILLISECOND, millisOfSecond);
        return calendar.getTime();
    }

    /**
     * 获取当地时间
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取当地时间
     * yyyy-mm-dd
     *
     * @return
     */
    public static Date nowYMD() {
        Date dt = now();
        return truncate(dt, Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当地时间字符串，返回yyyy-MM-dd格式
     *
     * @return
     */
    public static String nowStr() {
        return DateFormatUtils.format(now());
    }

    /**
     * 获取时间间隔的天数，不足一天算一天
     *
     * @param startInclude
     * @param endInclude
     * @return 返回间隔的天数，返回为正数
     */
    public static int daysBetween(Date startInclude, Date endInclude) {
        Preconditions.checkNotNull(startInclude, "data1 should not be null");
        Preconditions.checkNotNull(endInclude, "date2 should not be null");


        return daysBetween(toLocalDate(startInclude), toLocalDate(endInclude));
    }

    public static int daysBetween(long startInclude, long endInclude) {
        return daysBetween(Instant.ofEpochMilli(startInclude), Instant.ofEpochMilli(endInclude));
    }

    public static int daysBetween(Calendar startInclude, Calendar endInclude) {
        return daysBetween(startInclude.getTime(), endInclude.getTime());
    }

    public static int daysBetween(Instant startInclude, Instant endInclude) {
        return daysBetween(toLocalDate(startInclude), toLocalDate(endInclude));
    }

    public static int daysBetween(LocalDate startInclude, LocalDate endInclude) {
        if (startInclude.compareTo(endInclude) <= 0) {
            return (int) ChronoUnit.DAYS.between(startInclude, endInclude.plusDays(1));
        } else {
            return (int) ChronoUnit.DAYS.between(endInclude, startInclude.plusDays(1));
        }
    }

    /**
     * getAge:(获取年龄)
     */
    public static int getAge(Date birthDay) {
        //年龄不能为负数
        Preconditions.checkArgument(birthDay.before(now()), "出生日期不能后于当前日期!");
        LocalDate birthDate = toLocalDate(birthDay);
        LocalDate now = toLocalDate(now());
        Period p = Period.between(birthDate, now);
        return p.getYears();
    }

    public static int getAge(Calendar birthDay) {
        return getAge(birthDay.getTime());
    }

    /**
     * 两个日期间隔
     *
     * @param date1
     * @param date2
     * @param field 支持的值：
     * @return
     */
    public static long period(Date date1, Date date2, int field) {
        Preconditions.checkNotNull(date1, "data1 should not be null");
        Preconditions.checkNotNull(date2, "date2 should not be null");

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return period(cal1, cal2, field);
    }

    public static long period(long cal1, long cal2, int field) {
        Calendar cal1Cal = Calendar.getInstance();
        cal1Cal.setTimeInMillis(cal1);
        Calendar cal2Cal = Calendar.getInstance();
        cal2Cal.setTimeInMillis(cal2);
        return period(cal1Cal, cal2Cal, field);
    }

    public static long period(Calendar cal1, Calendar cal2, int field) {
        Preconditions.checkNotNull(cal1, "cal1 should not be null");
        Preconditions.checkNotNull(cal2, "cal2 should not be null");

        if (cal1.equals(cal2)) {
            return 0L;
        }

        Calendar mc;
        Calendar sc;
        switch (field) {
            case YEAR:
                return cal1.get(YEAR) - cal2.get(YEAR);
            case MONTH:
                long years = cal1.get(YEAR) - cal2.get(YEAR);
                long months = cal1.get(MONTH) - cal2.get(MONTH);
                return years * 12 + months;
            case DATE:
                mc = truncate(cal1, DATE);
                sc = truncate(cal2, DATE);
                return (mc.getTimeInMillis() - sc.getTimeInMillis()) / MILLIS_PER_DAY;
            case HOUR:
                mc = truncate(cal1, HOUR);
                sc = truncate(cal2, HOUR);
                return (mc.getTimeInMillis() - sc.getTimeInMillis()) / MILLIS_PER_HOUR;
            case MINUTE:
                mc = truncate(cal1, MINUTE);
                sc = truncate(cal2, MINUTE);
                return (mc.getTimeInMillis() - sc.getTimeInMillis()) / MILLIS_PER_MINUTE;
            case SECOND:
                mc = truncate(cal1, SECOND);
                sc = truncate(cal2, SECOND);
                return (mc.getTimeInMillis() - sc.getTimeInMillis()) / MILLIS_PER_SECOND;
            case MILLISECOND:
                return cal1.getTimeInMillis() - cal2.getTimeInMillis();
        }

        throw new IllegalArgumentException("field:" + field + " not supported!");
    }

    public static Timestamp addDays(Timestamp ts, int days) {
        return new Timestamp(new DateTime(ts.getTime()).plusDays(days).getMillis());
    }

    public static Date toDate(java.time.LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(java.time.LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static java.time.LocalDate toLocalDate(Instant date) {
        return toLocalDate(Date.from(date));
    }

    public static java.time.LocalDate toLocalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return LocalDate.of(calendar.get(YEAR), calendar.get(MONTH) + 1, calendar.get(DATE));
    }

    public static java.time.LocalDateTime toLocalDateTime(Date date) {
        return toLocalDateTime(new Timestamp(date.getTime()));
    }

    @SuppressWarnings("deprecation")
    public static Timestamp toSqlTimestamp(java.time.LocalDateTime dateTime) {
        return new Timestamp(
                dateTime.getYear() - 1900,
                dateTime.getMonthValue() - 1,
                dateTime.getDayOfMonth(),
                dateTime.getHour(),
                dateTime.getMinute(),
                dateTime.getSecond(),
                dateTime.getNano());
    }

    @SuppressWarnings("deprecation")
    public static java.time.LocalDateTime toLocalDateTime(Timestamp sqlTimestamp) {
        return LocalDateTime.of(
                sqlTimestamp.getYear() + 1900,
                sqlTimestamp.getMonth() + 1,
                sqlTimestamp.getDate(),
                sqlTimestamp.getHours(),
                sqlTimestamp.getMinutes(),
                sqlTimestamp.getSeconds(),
                sqlTimestamp.getNanos());
    }

    /**
     * 检查开始时间是否在当前时间之前
     * @param startTime "2019-11-07 12:00-13:00"
     * @return
     */
    public static Boolean checkStartTimeBeforeNow(String startTime) {
        String[] st = startTime.split(" ");
        String endTime = Joiner.on(" ").join(st[0], st[1].split("-")[1]);
        Date date = DateFormatUtils.parse(endTime,DateFormatUtils.ISO_DATETIME_NOSEC_PATTERN);
        return date.before(new Date());
    }
}