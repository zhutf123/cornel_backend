package com.demai.cornel.purcharse.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:33
*/
@Data
public class WaybillInfo {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * 承运人ID
    */
    private String carrierId;

    /**
    * 运输方式
    */
    private String transportId;

    /**
    * 起始地址
    */
    private String fromLocationId;

    /**
    * 终止地址
    */
    private String toLocationId;

    /**
    * 开始运输时间
    */
    private Timestamp startTime;

    /**
    * 接货时间
    */
    private Timestamp endTime;

    /**
    * 货物重量
    */
    private BigDecimal weight;

    /**
    * 重量单位
    */
    private String unitWeight;

    /**
    * 运费
    */
    private BigDecimal price;

    /**
    * 价格单位
    */
    private String unitPrice;

    /**
    * 货物ID
    */
    private String cargoId;

    /**
    * 父货物ID
    */
    private String parentCargoId;

    /**
    * 状态 0待运输 1运输中 2 运输完成 3 取消
    */
    private Integer status;

    /**
    * 更新时间
    */
    private Timestamp updateTime;


}