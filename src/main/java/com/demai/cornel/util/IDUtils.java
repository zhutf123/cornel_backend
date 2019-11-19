package com.demai.cornel.util;

import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * Created by haoling.wang on 2018/12/29.
 */
public class IDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getUUIDUpper() {
        return getUUID().toUpperCase();
    }

    public static String generateLikeId(String userId, String objectId) {
        String content = userId + "|" + objectId;
        return DigestUtils.md5DigestAsHex(content.getBytes());
    }

    public static String getUUIDWithType(String type) {
        return String.join("_", type, getUUIDUpper());
    }



}
