package com.demai.cornel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-06    21:52
 */
@ConfigurationProperties(prefix = "service.mobile")
@Service
public class ServiceMobileConfig {
    List<String> number = new ArrayList<>();

    public List<String> getNumber() {
        return number;
    }

    public void setNumber(List<String> number) {
        this.number = number;
    }
}
