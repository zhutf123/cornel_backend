package com.demai.cornel.vo.task;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2019-12-20    20:18
 */
@Data
public class ArriveDepDriverResp {

    /**
     * orderId : 123123
     * success : true
     * status : 2
     * verCode : 13df
     */

    private String orderId;
    private boolean success;
    private Long status;
    private String verCode;
    private String pickUpCode;

}
