/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

/**
 * Create By zhutf 19-9-30 下午5:37
 */
@SpringBootApplication(scanBasePackages = {
        "com.demai.cornel"
}, excludeName = {
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
@EnableConfigurationProperties
@EnableCaching
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass=true)
//@ImportResource("classpath:spring/dubbo-client.xml")
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class CornelBackendApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CornelBackendApplication.class);
    }


//    @Bean
//    public ServletRegistrationBean<Servlet> cacheAdminServlet() {
//        CacheAdminServlet cacheAdminServlet = new CacheAdminServlet();
//        ServletRegistrationBean<Servlet> registration = new ServletRegistrationBean<Servlet>();
//        registration.setServlet(cacheAdminServlet);
//        registration.addUrlMappings("/-/admin/cache");
//        return registration;
//    }

//    @Bean
//    public ServletRegistrationBean<Servlet> hystrixMetricsStreamServlet() {
//        HystrixMetricsStreamServlet hystrixMetricsStreamServlet = new HystrixMetricsStreamServlet();
//        ServletRegistrationBean<Servlet> registration = new ServletRegistrationBean<Servlet>();
//        registration.setServlet(hystrixMetricsStreamServlet);
//        registration.addUrlMappings("/hystrix.stream");
//        return registration;
//    }

//    @Bean
//    public OKHttpEngine okHttpEngine(@Value("${http.maxIdle}") int maxIdle,
//                                     @Value("${http.keepAlive}") int keepAlive,
//                                     @Value("${http.connectTimeout}") int connectTimeout,
//                                     @Value("${http.readTimeout}") int readTimeout,
//                                     @Value("${http.writeTimeout}") int writeTimeout) {
//        SimpleHttpsContext simpleHttpsContext = new SimpleHttpsContext("SSL");
//        EngineInitParam httpInit = new EngineInitParam(maxIdle, keepAlive, connectTimeout, readTimeout, writeTimeout, simpleHttpsContext);
//        return new OKHttpEngine(httpInit);
//    }

//    @Bean
//    public Sedis sedisInstance(@Value("${sedis.namespace}") String ns,
//                               @Value("${sedis.cipher}") String cipher,
//                               @Value("${sedis.zk-address}") String zkAddress) {
//        if (StringUtils.isBlank(zkAddress) || StringUtils.equals(zkAddress, "null")) {
//            return new Sedis(ns, cipher);
//        } else {
//            return new Sedis(ns, cipher, zkAddress);
//        }
//    }

//    @Bean
//    public SpringContext springContext() {
//        return new SpringContext();
//    }
//
//    @Bean
//    public SysInitBean sysInitBean() {
//        return new SysInitBean();
//    }

//    @Bean
//    public QuotaAspect quotaAspect(@Autowired Sedis sedis) {
//        QuotaAspect quotaAspect = new QuotaAspect();
//        quotaAspect.setSedis(sedis);
//        return quotaAspect;
//    }
}

