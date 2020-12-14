/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.interceptor;

import com.demai.cornel.annotation.CheckUserRole;
import com.demai.cornel.dao.UserRoleInfoDao;
import com.demai.cornel.model.UserRoleInfo;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.MD5Util;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.util.json.JsonUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.hp.gagawa.java.elements.B;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Create By zhutf 19-11-9 上午10:37
 * @author tfzhu
 */
@Slf4j
@Component
public class CheckRoleInterceptor implements HandlerInterceptor {

    @Resource private UserRoleInfoDao userRoleInfoDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod method = (HandlerMethod) handler;
        CheckUserRole accessControl = method.getMethodAnnotation(CheckUserRole.class);
        if (accessControl == null) {
            return Boolean.TRUE;
        }
        String url = request.getRequestURI();
        String checkRole = accessControl.checkRole();
        if (StringUtils.isBlank(checkRole)) {
            return Boolean.TRUE;
        }
        if (log.isDebugEnabled()) {
            log.debug("url:{} method：{} checkRole:{}", url, method.getMethod().getName(), checkRole);
        }

        List<UserRoleInfo> userRoleInfos = userRoleInfoDao.getRolesByUserId(CookieAuthUtils.getCurrentUser());
        if (CollectionUtils.isEmpty(userRoleInfos)) {
            return Boolean.FALSE;
        }
        List<String> roleIds = userRoleInfos.stream().map(role ->role.getRoleId()).collect(Collectors.toList());
        Boolean result = Boolean.FALSE;

        switch (checkRole){
            case "bu_op":
                if (roleIds.contains("4")){
                    result = Boolean.TRUE;
                }
                break;

            case "fin_op":
                if (roleIds.contains("5")){
                    result = Boolean.TRUE;
                }
        }
        return result;
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
