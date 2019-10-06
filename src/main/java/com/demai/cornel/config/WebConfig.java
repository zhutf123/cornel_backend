//package com.demai.cornel.config;
//
//import java.util.List;
//
//import com.demai.cornel.conver.CustomXmlMessageConverter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        configurer.defaultContentType(MediaType.APPLICATION_JSON).ignoreAcceptHeader(true)
//                .ignoreUnknownPathExtensions(false);
//    }
//
//    @Autowired
//    private CustomXmlMessageConverter customXmlMessageConverter;
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(customXmlMessageConverter);
//    }
//}
