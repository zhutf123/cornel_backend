package com.demai.cornel.config;

import com.google.common.base.Splitter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-06    21:52
 */
@ConfigurationProperties(prefix = "service")
@Service
public class ServiceMobileConfig {
    private String mobileList;



    public String getMobileList() {
        return mobileList;
    }

    public void setMobileList(String mobileList) {
        this.mobileList = mobileList;
    }

    public static void setServiceMobile(List<String> serviceMobile) {
        ServiceMobileConfig.serviceMobile = serviceMobile;
    }

    public static List<String> serviceMobile = new ArrayList<>();

    @PostConstruct
    private void init(){
        if(mobileList!=null){
            serviceMobile.addAll(Splitter.on(",").trimResults().splitToList(mobileList));
        }
    }
}
