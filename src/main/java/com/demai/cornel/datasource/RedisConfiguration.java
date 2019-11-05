package com.demai.cornel.datasource;


import java.util.List;
import java.util.stream.Collectors;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.common.base.Splitter;

@Configuration
public class RedisConfiguration {

    public static final Splitter COMMA_SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();

    @Bean
    public RedisConnectionFactory redisConnectionFactory(
            @Autowired AbstractEnvironment abstractEnvironment,
            @Value("${spring.redis.password}") String password) {
        org.springframework.core.env.PropertySource<?> propertySource = abstractEnvironment.getPropertySources().get("applicationConfig: [classpath:/application-default.yml]");
        if (null != propertySource) {
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(propertySource);
            redisClusterConfiguration.setPassword(password);
            LettuceConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration);
            return redisConnectionFactory;
        }
        throw new RuntimeException("redis集群注册服务异常");
    }

    @Bean
    public RedissonClient redissonClient(@Autowired AbstractEnvironment abstractEnvironment) {
        PropertySource<?> propertySource = abstractEnvironment.getPropertySources()
                .get("applicationConfig: [classpath:/application-default.yml]");
        if(propertySource == null){
            throw new RuntimeException("redissonClient注册异常 未找到配置文件");
        }
        String nodes = String.valueOf(propertySource.getProperty("spring.redis.host"));
        List<String> nodeList = COMMA_SPLITTER.splitToList(nodes).stream().map(item -> "redis://" + item)
                .collect(Collectors.toList());
        Config config = new Config();
        config.useClusterServers()
                .setPassword(String.valueOf(propertySource.getProperty("spring.redis.password")))
                .addNodeAddress(nodeList.toArray(new String[nodeList.size()]));
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate (@Autowired RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public HashOperations hashOperations(@Autowired StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForHash();
    }

}
