package com.demai.cornel.auth.interceptors;

import com.demai.cornel.auth.annotation.Authority;
import com.demai.cornel.auth.service.impl.UrlAclServiceImpl;
import com.demai.cornel.auth.service.impl.UserServiceImpl;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.util.CookieAuthUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.ref.PhantomReference;
import java.util.List;

/**
 * bin.zhang 2019
 */
@Slf4j
public class AuthCheckInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UrlAclServiceImpl urlAclService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        HandlerMethod method = (HandlerMethod) handler;
        Authority methodAnnotation = method.getMethodAnnotation(Authority.class);
        if (methodAnnotation == null) {
            log.info("url [{}] No permission authentication required ", url);
            return true;
        }
        String userId = UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME);
        return urlAclService.checkUserUrlAcls(userId, url);
    }


    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }
}
