package com.demai.cornel.model;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    12:02
 */
@Data
public class ImgInfoReq {
    private String key;
    private String url;

    public ImgInfoReq(String key, String url) {
        this.key = key;
        if(url==null){
            this.url = "";
        }else {
            this.url = url;

        }
    }

    public ImgInfoReq() {
    }
}
