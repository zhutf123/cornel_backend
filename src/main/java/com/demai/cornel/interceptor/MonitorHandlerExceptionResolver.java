package com.demai.cornel.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @Author binz.zhang
 * @Date: 2019-11-05    13:48
 */
@Component
public class MonitorHandlerExceptionResolver implements HandlerExceptionResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorHandlerExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LOGGER.error("global exception ", ex);
        return null;
    }
}