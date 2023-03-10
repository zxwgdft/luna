package com.luna.framework.utils;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 时间处理工具类（未考虑其他时区及夏令时问题）
 * <p>
 * 星期一至星期日分别由1-7代表
 * <p>
 * 注：LocalDate处理日期问题比Calendar性能更好
 */
@SuppressWarnings("ALL")
public class TimeUtil {

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    // 时区偏移对象
    private static final ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
    // 时区偏移量（毫秒）
    private static final long millisOffset = zoneOffset.getTotalSeconds() * 1000L;

    public static final Date MAX_DATE = Date.from(LocalDate.parse("9999-12-31").atStartOfDay().toInstant(zoneOffset));


    public static long getOffset() {
        //return TimeZone.getDefault().getOffset(...);
        /** 由于国内不存在夏令时，所以可以直接获取时区偏移量，如果需要可改为TimeZone去获取偏移量*/
        return millisOffset;
    }

    /**
     * 时间戳转LocalDate
     */
    public static LocalDate toLocalDate(long millis) {
        return LocalDate.ofEpochDay((millis + getOffset()) / MILLIS_IN_DAY);
    }

    /**
     * 日期转LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return LocalDate.ofEpochDay((date.getTime() + getOffset()) / MILLIS_IN_DAY);
    }

    /**
     * 日期转LocalTime
     */
    public static LocalTime toLocalTime(Date date) {
        long secondOfDay = (date.getTime() + getOffset()) % MILLIS_IN_DAY / 1000;
        return LocalTime.ofSecondOfDay(secondOfDay);
    }

    /**
     * 快速判断两个时间是否同一天
     *
     * @param d1 日期
     * @param d2 日期
     * @return 同一天true，否在false
     */
    public static boolean isSameDay(final Date d1, final Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        return isSameDay(d1.getTime(), d2.getTime());
    }

    /**
     * 快速判断两个时间是否同一天
     *
     * @param ms1 时间戳1
     * @param ms2 时间戳2
     * @return 同一天true，否在false
     */
    public static boolean isSameDay(final long ms1, final long ms2) {
        final long interval = Math.abs(ms1 - ms2);
        return interval < MILLIS_IN_DAY && getDays(ms1) == getDays(ms2);
    }

    /**
     * 获取天数（相对1970年）
     *
     * @param millis 毫秒数
     * @return 相对1970年1月1日计算机时间起始日期过了多少天
     */
    public static long getDays(long millis) {
        // 除以一天的毫秒数等于相对的天数，但是需要考虑时区问题
        return (millis + getOffset()) / MILLIS_IN_DAY;
    }

    /**
     * 通过相对天数获取日期
     *
     * @param days 相对1970年1月1日计算机时间起始日期过了多少天
     * @return 对应日期
     */
    public static Date getDateByDays(long days) {
        return new Date(days * MILLIS_IN_DAY - getOffset());
    }


    /**
     * 获取时间戳中对应时分秒的毫秒数
     *
     * @param millis 毫秒数
     */
    public static int getMillisecondsInDay(long millis) {
        long dayTimeMillis = (millis + getOffset()) % MILLIS_IN_DAY;
        return (int) dayTimeMillis;
    }

    /**
     * 获取时间戳中对应时分秒的秒数
     *
     * @param millis 毫秒数
     */
    public static int getSecondsInDay(long millis) {
        long dayTimeMillis = (millis + getOffset()) % MILLIS_IN_DAY;
        return (int) (dayTimeMillis / 1000);
    }

    /**
     * 获取时间戳中对应时分秒的分钟数
     *
     * @param millis 毫秒数
     */
    public static int getMinutesInDay(long millis) {
        long dayTimeMillis = (millis + getOffset()) % MILLIS_IN_DAY;
        return (int) (dayTimeMillis / 60000);
    }


    /**
     * 获取去除了时分秒的日期
     *
     * @param millis 日期时间戳
     * @return 去除了时分秒毫秒数的日期
     */
    public static Date toDate(long millis) {
        millis = millis - ((millis + getOffset()) % MILLIS_IN_DAY);
        return new Date(millis);
    }

    /**
     * 获取去除了时分秒的日期
     *
     * @param date 日期
     * @return 去除了时分秒毫秒数的日期
     */
    public static Date toDate(Date date) {
        return toDate(date.getTime());
    }

    /**
     * 获取日期+时间
     *
     * @param date   日期
     * @param millis 时间对应毫秒数
     */
    public static Date toDateTime(Date date, long millis) {
        if (date == null) {
            return null;
        }
        long dateMillis = date.getTime();
        dateMillis = dateMillis - ((dateMillis + getOffset()) % MILLIS_IN_DAY);
        dateMillis = dateMillis + millis;
        return new Date(dateMillis);
    }

    /**
     * 获取时间段相差天数
     *
     * @param startMillis 开始时间戳
     * @param endMillis   结束时间戳
     * @return 日期间隔天数
     */
    public static long getIntervalDays(long startMillis, long endMillis) {
        startMillis = startMillis - ((startMillis + getOffset()) % MILLIS_IN_DAY);
        long endd = (endMillis + getOffset()) % MILLIS_IN_DAY;
        if (endd == 0) {
            return (endMillis - startMillis) / MILLIS_IN_DAY;
        } else {
            return (endMillis - endd - startMillis) / MILLIS_IN_DAY + 1;
        }
    }

    /**
     * 获取时间段内流水号列表
     *
     * @param startMillis 开始时间戳
     * @param endMillis   结束时间戳
     * @return 时间间隔内流水号
     */
    public static List<Integer> getSerialNumberByDay(long startMillis, long endMillis) {
        List<Integer> sns = new ArrayList<>();
        long startDay = (startMillis + getOffset()) / MILLIS_IN_DAY;
        long endDay = (endMillis + getOffset()) / MILLIS_IN_DAY;

        while (endDay >= startDay) {
            sns.add(getSerialNumberByDay(LocalDate.ofEpochDay(startDay)));
            startDay++;
        }

        return sns;
    }

    /**
     * 获取流水号
     *
     * @param millis 日期时间戳
     * @return 日期流水号
     */
    public static int getSerialNumberByDay(long millis) {
        return getSerialNumberByDay(toLocalDate(millis));
    }

    /**
     * 获取流水号
     */
    public static int getSerialNumberByDay(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        return year * 10000 + month * 100 + day;
    }

    /**
     * 获取月流水号
     */
    public static int getSerialNumberByMonth(long millis) {
        return getSerialNumberByMonth(toLocalDate(millis));
    }

    /**
     * 获取月流水号
     */
    public static int getSerialNumberByMonth(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        return year * 100 + month;
    }

    /**
     * 是否今天
     */
    public static boolean isToday(long millis) {
        return isSameDay(millis, System.currentTimeMillis());
    }

    /**
     * 是否今天或以后的时间
     */
    public static boolean isAfterOrEqualToday(long millis) {
        millis = millis - ((millis + getOffset()) % MILLIS_IN_DAY);
        long nowMillis = System.currentTimeMillis();
        nowMillis = nowMillis - ((nowMillis + getOffset()) % MILLIS_IN_DAY);
        return millis >= nowMillis;
    }

    /**
     * 获取今天过去某天
     *
     * @param pastDays 过去多少天，负数则为未来多少天
     */
    public static Date getTodayBefore(int pastDays) {
        return getDateBefore(System.currentTimeMillis(), pastDays);
    }

    /**
     * 获取某天的过去几天
     *
     * @param millis   时间戳
     * @param pastDays 过去多少天，负数则为未来多少天
     */
    public static Date getDateBefore(long millis, int pastDays) {
        millis = millis - ((millis + getOffset()) % MILLIS_IN_DAY);
        return new Date(millis - MILLIS_IN_DAY * pastDays);
    }

    /**
     * 获取某天的过去几年
     *
     * @param millis    时间戳
     * @param pastYears 过去多少年
     */
    public static Date getDateBeforeYear(long millis, int pastYears) {
        LocalDate date = toLocalDate(millis);
        return Date.from(date.plusYears(-pastYears).atStartOfDay().toInstant(zoneOffset));
    }

    /**
     * 获取某天的过去几月
     *
     * @param millis     时间戳
     * @param pastMonths 过去多少月
     */
    public static Date getDateBeforeMonth(long millis, int pastMonths) {
        LocalDate date = toLocalDate(millis);
        return Date.from(date.plusMonths(-pastMonths).atStartOfDay().toInstant(zoneOffset));
    }

    /**
     * 获取某天的过去几年
     *
     * @param date      时间
     * @param pastYears 过去多少年
     */
    public static Date getDateBeforeYear(Date date, int pastYears) {
        LocalDate localDate = toLocalDate(date);
        return Date.from(localDate.plusYears(-pastYears).atStartOfDay().toInstant(zoneOffset));
    }

    /**
     * 获取昨天
     */
    public static Date getYesterday() {
        return getDateBefore(System.currentTimeMillis(), 1);
    }

    /**
     * 获取年份
     */
    public static int getYear(Date date) {
        return LocalDate.ofEpochDay((date.getTime() + getOffset()) / MILLIS_IN_DAY).getYear();
    }

    /**
     * 获取当前年份
     */
    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    /**
     * 获取月份（12）
     */
    public static int getMonth(Date date) {
        return LocalDate.ofEpochDay((date.getTime() + getOffset()) / MILLIS_IN_DAY).getMonthValue();
    }

    /**
     * 获取当前月份（12）
     */
    public static int getCurrentMonth() {
        return LocalDate.now().getMonthValue();
    }

    /**
     * 获取月中日期
     */
    public static int getDay(Date date) {
        return LocalDate.ofEpochDay((date.getTime() + getOffset()) / MILLIS_IN_DAY).getDayOfMonth();
    }


    /**
     * 获取当前月中日期
     */
    public static int getCurrentDay() {
        return LocalDate.now().getDayOfMonth();
    }

    /**
     * 获取星期几(周一到周7)
     */
    public static int getWeekDay(Date date) {
        return LocalDate.ofEpochDay((date.getTime() + getOffset()) / MILLIS_IN_DAY).getDayOfWeek().getValue();
    }

    /**
     * 获取小时（24）
     */
    public static int getHour(Date date) {
        return LocalTime.ofSecondOfDay(((date.getTime() + getOffset()) % MILLIS_IN_DAY / 1000)).getHour();
    }

    /**
     * 获取当前小时（24）
     */
    public static int getCurrentHour() {
        return LocalTime.now().getHour();
    }

    /**
     * 获取当前时间最近一个星期几(1-7)
     */
    public static Date getLastWeekDay(int weekDay) {
        return getLastWeekDay(null, weekDay);
    }

    /**
     * 获取时间最近一个星期几(1-7)
     */
    public static Date getLastWeekDay(Date date, int weekDay) {
        int dayWeek = LocalDate.ofEpochDay((date.getTime() + getOffset()) / MILLIS_IN_DAY).getDayOfWeek().getValue();
        int before = dayWeek - weekDay + 7;
        if (before >= 7) {
            before -= 7;
        }
        return getDateBefore(date.getTime(), before);
    }

    /**
     * 是否日期为星期几
     */
    public static boolean isWeekDay(Date date, int weekDay) {
        return LocalDate.ofEpochDay((date.getTime() + getOffset()) / MILLIS_IN_DAY).getDayOfWeek().getValue() == weekDay;
    }

    /**
     * 两个日期是否在同一个星期
     */
    public static boolean inSameWeek(Date d1, Date d2) {
        long t1 = d1.getTime();
        long t2 = d2.getTime();

        t1 -= t1 % MILLIS_IN_DAY;
        t2 -= t2 % MILLIS_IN_DAY;

        long d = Math.abs(t1 - t2);

        if (d > MILLIS_IN_DAY * 7) {
            return false;
        }

        int day1 = (int) ((t1 / MILLIS_IN_DAY + 4) % 7);
        int day2 = (int) ((t2 / MILLIS_IN_DAY + 4) % 7);

        if (day1 == 0) {
            day1 = 7;
        }
        if (day2 == 0) {
            day2 = 7;
        }

        int x = Math.abs(day1 - day2);

        return x == (int) (d / MILLIS_IN_DAY);
    }

    /**
     * 根据出生日期计算年龄
     */
    public static int getAge(Date birthDay) {
        if (Objects.isNull(birthDay) || birthDay.getTime() > System.currentTimeMillis()) {
            return 0;
        }

        LocalDate now = LocalDate.now();

        int yearNow = now.getYear();
        int monthNow = now.getMonthValue();
        int dayOfMonthNow = now.getDayOfMonth();

        LocalDate dateTime = LocalDate.ofEpochDay((birthDay.getTime() + getOffset()) / MILLIS_IN_DAY);

        int yearBirth = dateTime.getYear();
        int monthBirth = dateTime.getMonthValue();
        int dayOfMonthBirth = dateTime.getDayOfMonth();

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

    /**
     * 获取某年某月中天数
     */
    @SuppressWarnings("AlibabaSwitchStatement")
    public static int getDaysOfMonth(int year, int month) {
        int days = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                if (year % 400 == 0 && (year % 4 == 0 || year % 100 != 0)) {
                    days = 29;
                } else {
                    days = 28;
                }
                break;
        }
        return days;
    }

    /**
     * 当年第一天
     */
    public static Date getFirstDayOfCurrentYear() {
        return Date.from(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay().toInstant(zoneOffset));
    }

    /**
     * 当年最后一天
     */
    public static Date getLastDayOfCurrentYear() {
        return Date.from(LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).atStartOfDay().toInstant(zoneOffset));
    }


    /**
     * 获取某年第一天
     */
    public static Date getFirstDayOfYear(int year) {
        return Date.from(LocalDate.of(year, 1, 1).with(TemporalAdjusters.firstDayOfYear()).atStartOfDay().toInstant(zoneOffset));
    }

    /**
     * 获取某年最后一天
     */
    public static Date getLastDayOfYear(int year) {
        return Date.from(LocalDate.of(year, 1, 1).with(TemporalAdjusters.lastDayOfYear()).atStartOfDay().toInstant(zoneOffset));
    }


    /**
     * 获取当月第一天日期
     */
    public static Date getFirstDayOfCurrentMonth() {
        return Date.from(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay().toInstant(zoneOffset));
    }

    /**
     * 获取当月最后一天日期
     */
    public static Date getLastDayOfCurrentMonth() {
        return Date.from(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay().toInstant(zoneOffset));
    }

    /**
     * 获取月份第一天日期
     *
     * @param year  年
     * @param month 月（1-12）
     */
    public static Date getFirstDayOfMonth(int year, int month) {
        return Date.from(LocalDate.of(year, month, 1).atStartOfDay().toInstant(zoneOffset));
    }

    /**
     * 获取月份最后一天日期
     *
     * @param year  年
     * @param month 月（1-12）
     */
    public static Date getLastDayOfMonth(int year, int month) {
        return Date.from(LocalDate.of(year, month, getDaysOfMonth(year, month)).atStartOfDay().toInstant(zoneOffset));
    }


    /**
     * 获取一天开始时间
     *
     * @param date 日期
     * @return {@link Date}
     */
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 获取一天结束时间
     *
     * @param date 日期
     * @return {@link Date}
     */
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取日期所在周为该年第几周
     *
     * @param date
     * @return
     */
    public static int getWeekBasedYear(Date date) {
        LocalDate localDate = TimeUtil.toLocalDate(date);
        return localDate.get(WeekFields.ISO.weekOfWeekBasedYear());
    }

}
