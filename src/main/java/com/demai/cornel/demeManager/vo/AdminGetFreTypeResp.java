package com.demai.cornel.demeManager.vo;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    23:58
 */
@Data
public class AdminGetFreTypeResp {
    private String key;
    private String value;
    private Integer type;

    public AdminGetFreTypeResp(String key, String value, Integer type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }
}
