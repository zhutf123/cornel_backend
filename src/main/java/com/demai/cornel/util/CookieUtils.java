package com.demai.cornel.util;

import com.google.common.base.Strings;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by binz.zhang on 2019/1/4.
 */
public class CookieUtils {

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies;
        if (ArrayUtils.isNotEmpty(cookies = request.getCookies())) {
            for (Cookie cookie : cookies) {
                // todo 需要去掉 2020-03-14以后就可以去掉了
                if (cookie.getName().equals(name) && !Strings.isNullOrEmpty(cookie.getPath()) && cookie.getPath().equals("/admin")) {
                    cookie.setMaxAge(0);
                }
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie;
        if (null != (cookie = getCookie(request, name))) {
            return cookie.getValue();
        }
        return null;
    }
}
