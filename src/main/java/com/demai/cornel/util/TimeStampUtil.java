package com.demai.cornel.util;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author binz.zhang
 * @Date: 2020-01-06    12:40
 */
@Slf4j public class TimeStampUtil {

    public static Timestamp stringConvertTimeStamp(String timeFormat, String time) {
        try {
            if (Strings.isNullOrEmpty(timeFormat) || Strings.isNullOrEmpty(time)) {
                return null;
            }
            Date d1 = new SimpleDateFormat(timeFormat).parse(time);//定义起始日期
            return new Timestamp(d1.getTime());
        } catch (Exception e) {
            log.error("stringConvertTimeStamp parse time error", e);
            return null;
        }
    }

    public static String timeStampConvertString(String timeFormat, Timestamp time) {
        try {
            return  new SimpleDateFormat(timeFormat).format(time.getTime());  // 获取只有年月日的时间
        } catch (Exception e) {
            log.error(" timeStampConvertString parse time error", e);
            return null;
        }
    }

    public static String timeStampConvertString(String timeFormat, Timestamp time) {
        try {
            if (Strings.isNullOrEmpty(timeFormat) || time==null) {
                return null;
            }
           return new SimpleDateFormat(timeFormat).format(time);
        } catch (Exception e) {
            log.error("parse time error", e);
            return null;
        }
    }
}
