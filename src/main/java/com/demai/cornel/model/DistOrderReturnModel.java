package com.demai.cornel.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2019-11-07    19:38
 * 派单结果的返回类
 */
@Data
public class DistOrderReturnModel {

    private String taskId;  //任务ID
    private Integer distNum; //派单数量
    private BigDecimal taskTotalUnDistWeight; //任务还剩余总的
    private BigDecimal distWeight; //派发货物量
    private BigDecimal unDistWeight; //task 剩余未派发的货物量


}
