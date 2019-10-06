/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util.json;

import com.demai.cornel.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create By zhutf  19-10-6  下午3:36
 */
public class JsonDateTimeUtil {

    public static Pattern r;

    static {
        r = Pattern.compile("/Date\\((-?\\d+)([+|-]\\d+)?\\)/");
    }

    public static DateTime parseJsonDate(String strJsonDate) {
        Matcher matcher = r.matcher(strJsonDate);
        if (matcher.matches()) {
            Long ms = null;
            if (matcher.groupCount() >= 1) {
                ms = Long.parseLong(matcher.group(1));
            }
            DateTimeZone dtz = DateTimeZone.UTC;
            if (matcher.groupCount() >= 2) {
                String dtzStr = matcher.group(2);
                if (StringUtils.isNotBlank(dtzStr)) {
                    dtz = DateTimeZone.forID(dtzStr);
                }
            }
            if (ms == null || ms == DateUtils.AD_ONE_DATE.getTime()) {
                return null;
            }
            return new DateTime(ms, dtz);
        }
        throw new IllegalArgumentException("转换jsonDateTime失败! text:" + strJsonDate);
    }

}
