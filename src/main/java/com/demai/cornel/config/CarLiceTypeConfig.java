package com.demai.cornel.config;

import com.google.common.base.Splitter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    15:43
 */
@ConfigurationProperties(prefix = "car-config")
@Service
public class CarLiceTypeConfig {
    private String carLiceType;
    public static List<String> carLiceTypeList = new ArrayList<>();

    public void setDownload(String download) {
        this.carLiceType = download;
    }
    @PostConstruct
    private void init(){
        if(carLiceType!=null){
            carLiceTypeList.addAll(Splitter.on(",").trimResults().splitToList(carLiceType));
        }
    }
}
