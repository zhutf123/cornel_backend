package com.demai.cornel.interceptor;

import com.demai.cornel.constant.ContextConsts;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.CookieUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.demai.cornel.util.CookieAuthUtils.KEY_USER_DOMAIN;
import static com.demai.cornel.util.CookieAuthUtils.KEY_USER_NAME;


/**
 * Created by binz.zhang on 2019/1/4.
 * 获取用户信息
 */
@Slf4j @CustomInterceptor(order = 2, addPathPatterns = { "/**" }, excludePathPatterns = { "/check.jsp" })
public class CookieInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        try {
            String cKey = CookieUtils.getCookieValue(request, ContextConsts.COOKIE_CKEY_NAME);
            if(Strings.isNullOrEmpty(cKey)){
                cKey = CookieUtils.getCookieValue(request, ContextConsts.COOKIE_CKEY_NAME_TALK);
            }
            if (Strings.isNullOrEmpty(cKey)) {
                log.warn("request not attach user cKey info");
                Map<String,String> defaultUserMap = Maps.newHashMap();
                defaultUserMap.put(KEY_USER_NAME, "System_Default");
                defaultUserMap.put(KEY_USER_DOMAIN, "ejabhost1");
                UserHolder.set(defaultUserMap);
            } else {
                Map<String, String> userInfoMap = CookieAuthUtils.getUserFromCKey(cKey);
                if (MapUtils.isNotEmpty(userInfoMap)) {
                    UserHolder.set(userInfoMap);
                    MDC.put(ContextConsts.MDC_USER, userInfoMap.get(KEY_USER_NAME));
                }
            }
        } catch (Exception e) {
            log.error("parse cookie info occur exception", e);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        UserHolder.remove();
    }

}
