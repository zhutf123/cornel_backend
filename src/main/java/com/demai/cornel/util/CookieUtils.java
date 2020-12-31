package com.demai.cornel.util;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by binz.zhang on 2019/1/4.
 */
@Slf4j
public class CookieUtils {

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies;
        if (ArrayUtils.isNotEmpty(cookies = request.getCookies())) {
            for (Cookie cookie : cookies) {
                // todo 需要去掉 2020-03-14以后就可以去掉了
                log.info("cookie path is {}, cookie is name is {}",cookie.getPath(),cookie.getName());
                if (cookie.getName().equals(name) && !Strings.isNullOrEmpty(cookie.getPath()) && cookie.getPath().equals("/admin")) {
                    log.info("delete this cookie");
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


    public static void copyCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies;
        if (ArrayUtils.isNotEmpty(cookies = request.getCookies())) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(24 * 60 * 60);
                response.addCookie(cookie);
            }
        }
    }
}
