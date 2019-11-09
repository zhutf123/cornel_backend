/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demai.cornel.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.demai.cornel.annotation.AccessControl;

/**
 * Create By zhutf 19-11-9 上午10:37
 */
@Slf4j
@Component
public class AccessControlnterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerMethod method = (HandlerMethod) handler;
        AccessControl accessControl = method.getMethodAnnotation(AccessControl.class);
        if (accessControl == null) {
            return Boolean.TRUE;
        }
        String url = request.getRequestURI();
        String control = accessControl.value();
        if (StringUtils.isBlank(control)) {
            return Boolean.TRUE;
        }
        if (log.isDebugEnabled()) {
            log.debug("url:{} method：{} params:{} access control:{}", url, method.getMethod().getName(),
                    JsonUtil.toJson(method.getMethodParameters()), control);
        }

        return Boolean.TRUE;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Object o, Exception e) throws Exception {
    }

}
