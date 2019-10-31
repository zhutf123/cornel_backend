package com.demai.cornel.config;

import java.util.Comparator;
import java.util.List;

import com.demai.cornel.auth.interceptors.AuthCheckInterceptor;
import com.demai.cornel.interceptor.CustomInterceptor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private List<HandlerInterceptor> handlerInterceptors;

    public WebConfig(List<HandlerInterceptor> handlerInterceptors){
        this.handlerInterceptors = handlerInterceptors;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (CollectionUtils.isNotEmpty(handlerInterceptors)) {
        handlerInterceptors.sort(Comparator.comparingInt(this::getOrder));
            for (HandlerInterceptor handlerInterceptor:handlerInterceptors) {
                    registry.addInterceptor(handlerInterceptor)
                            .addPathPatterns(getAddPathPatterns(handlerInterceptor))
                            .excludePathPatterns(getExcludePathPatterns(handlerInterceptor));
            }
    }
    }


    private int getOrder(HandlerInterceptor handlerInterceptor) {
        CustomInterceptor annotation = AnnotationUtils.findAnnotation(handlerInterceptor.getClass(), CustomInterceptor.class);
        if (annotation == null) {
            return Ordered.LOWEST_PRECEDENCE;
        }
        return annotation.order();
    }
    private String[] getAddPathPatterns(HandlerInterceptor handlerInterceptor) {
        CustomInterceptor annotation = AnnotationUtils.findAnnotation(handlerInterceptor.getClass(), CustomInterceptor.class);
        if (annotation == null) {
            return null;
        }
        return annotation.addPathPatterns();
    }

    private String[] getExcludePathPatterns(HandlerInterceptor handlerInterceptor) {
        CustomInterceptor annotation = AnnotationUtils.findAnnotation(handlerInterceptor.getClass(), CustomInterceptor.class);
        if (annotation == null) {
            return null;
        }
        return annotation.excludePathPatterns();
    }
}
