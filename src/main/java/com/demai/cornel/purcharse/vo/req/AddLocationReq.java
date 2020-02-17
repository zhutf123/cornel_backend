package com.demai.cornel.purcharse.vo.req;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-17    11:21
 */
@Data
public class AddLocationReq {
    private String locationId;
    private String location;
    private String locationArea;
    private String locationDetail;
    private Integer defaultFlag = 0;
}
