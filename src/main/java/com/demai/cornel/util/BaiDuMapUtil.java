package com.demai.cornel.util;

import com.demai.cornel.model.BaiDuGeCodingResp;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author binz.zhang
 * @Date: 2019-12-30    14:20
 * 百度地图api
 */
@Slf4j public class BaiDuMapUtil {
    //@Value("baidu.ak")
    private static String BAIDU_AK = "1EBYcarYLWPnnpUbrX4SUxQwtLSjgvF0";
    private static String MAP_API = "http://api.map.baidu.com";

    public static BaiDuGeCodingResp getGisByLocation(String location) throws UnsupportedEncodingException {

        Map paramsMap = new LinkedHashMap<String, String>();
        paramsMap.put("address", location);
        paramsMap.put("output", "json");
        paramsMap.put("ak", BAIDU_AK);
        String paramsStr = BaiDuMapUtil.toQueryString(paramsMap);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MAP_API).append("/geocoding/v3/?").append(paramsStr);
        BaiDuGeCodingResp baiDuGeCodingResp = null;
        try {
            String apiRest = HttpClientUtils.get(stringBuilder.toString());
            log.debug("call bai du map api of /geocoding/v3 return is [{}]", apiRest);
            if (!Strings.isNullOrEmpty(apiRest)) {
                baiDuGeCodingResp = JacksonUtils.string2Obj(apiRest, BaiDuGeCodingResp.class);
            }
        } catch (Exception e) {
            log.info("call baiDu map api geocoding/v3/ error ", e);
            return null;
        }
        return baiDuGeCodingResp;

    }

    // 对Map内所有value作utf8编码，拼接返回结果
    public static String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        for (Map.Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey() + "=");
            queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    // 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String loaction = "北京市";
        BaiDuGeCodingResp baiDuGeCodingResp = BaiDuMapUtil.getGisByLocation(loaction);
        System.out.println("ok");
    }
}
