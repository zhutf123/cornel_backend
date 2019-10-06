package com.demai.cornel.util;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public DefaultHttpClient httpClient() {
        HttpParams params = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 120000);

        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUserAgent(params, "Mozilla/5.0 (Windows NT 6.1; WOW64) Chrome/27.0.1453.94 Safari/537.36 qunarhc/8.0.1");

        HttpClientParams.setCookiePolicy(params, CookiePolicy.IGNORE_COOKIES);

        PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
        cm.setMaxTotal(500);
        cm.setDefaultMaxPerRoute(200);

        DefaultHttpClient client = new DefaultHttpClient(cm, params);
//        Interceptors.gzip(client, true);
//        Strategies.keepAlive(client, 5000);

        return client;
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public RestTemplate restTemplate() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Lists.newArrayList(MediaType.ALL));
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
        restTemplate.getMessageConverters().add(converter);
        return restTemplate;
    }
}
