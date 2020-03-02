package com.demai.cornel.config;

import com.google.common.base.Splitter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-07    22:34
 */
@ConfigurationProperties(prefix = "banner")
@Service
public class BannerConfig {
    private String download;
    private String purchaseBanner;
    public static List<String> downloadUrl = new ArrayList<>();
    public static List<String> purchaseBannerLsit = new ArrayList<>();

    public void setPurchaseBanner(String purchaseBanner) {
        this.purchaseBanner = purchaseBanner;
    }

    public void setDownload(String download) {
        this.download = download;
    }
    @PostConstruct
    private void init(){
        if(download!=null){
            downloadUrl.addAll(Splitter.on(",").trimResults().splitToList(download));
        }
        if(download!=null){
            purchaseBannerLsit.addAll(Splitter.on(",").trimResults().splitToList(purchaseBanner));
        }

    }
}
