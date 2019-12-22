package com.demai.cornel.config;

import com.demai.cornel.interceptor.CustomInterceptor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Comparator;
import java.util.List;

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
            return new String[]{};
        }
        return annotation.addPathPatterns();
    }

    private String[] getExcludePathPatterns(HandlerInterceptor handlerInterceptor) {
        CustomInterceptor annotation = AnnotationUtils.findAnnotation(handlerInterceptor.getClass(), CustomInterceptor.class);
        if (annotation == null) {
            return new String[]{};
        }
        return annotation.excludePathPatterns();
    }
}
