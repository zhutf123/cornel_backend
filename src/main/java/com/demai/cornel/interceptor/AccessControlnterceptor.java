/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demai.cornel.util.MD5Util;
import com.demai.cornel.util.json.JsonUtil;
import com.google.common.base.Splitter;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.demai.cornel.annotation.AccessControl;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Create By zhutf 19-11-9 上午10:37
 */
@Slf4j
@Component
public class AccessControlnterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
        String key = MD5Util.MD5Encode(JsonUtil.toJson(RequestMethodParam2MD5.builder().url(url)
                .method(method.getMethod().getName()).params(JsonUtil.toJson(method.getMethodParameters())).build()),
                "UTF-8");
        long number = stringRedisTemplate.opsForValue().increment(key, 1);
        List<Long> param = Splitter.on("_").splitToList(control).stream().map(num -> Long.parseLong(num))
                .collect(Collectors.toList());
        if (param.size() != 2) {
            log.error("access control set error {}", control);
            return Boolean.TRUE;
        }
        if (number == 1) {
            stringRedisTemplate.expire(key, param.get(0), TimeUnit.SECONDS);
        }
        if (number > param.get(1)){
            log.error("access control over times {} in {} seconds",param.get(1),param.get(0));
            return Boolean.FALSE;
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

    @Data
    @Builder
    public static class RequestMethodParam2MD5 implements Serializable {
        private String url;
        private String method;
        private String params;
    }

}
