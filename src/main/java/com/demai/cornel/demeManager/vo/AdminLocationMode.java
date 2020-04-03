package com.demai.cornel.demeManager.vo;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    19:02
 */
@Data public class AdminLocationMode {
    private String locationArea;
    private String locationDetail;
    private String location;
    private String locationId;
    private Integer systemFlag = 0;
    private Integer status=1;
}
