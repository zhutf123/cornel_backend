package com.demai.cornel.vo.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2019-12-20    20:56
 * 司机侧获取订单info
 */
@Data
public class GetOrderInfoResp {

    /**
     * orderId : 123123
     * taskId : 12312
     * title : 黑龙江丰收农场一级玉米
     * dep : 黑龙江省鹤岗市xxxx
     * arr : 青岛琅琊码头
     * depGis : xxxxx,xxxx
     * arrGis : xxxxx,xxxx
     * startValidDate : 2019-11-07
     * endValidDate : 2019-11-10
     * supplierMobile : 13269712016
     * lorryId : 123123
     * plateNumber : 黑A123456
     * unitDistance : km
     * carryWeight : 30
     * overCarryWight : 120
     * verifyCode : 5555
     * serviceMobile : 010-123456789
     * distance : 500.0
     * orderWeight : 80
     * price : 50
     * unitWeight : 吨
     * unitPrice : 元
     * startTime : 2019-11-07 12:00-13:00
     * status : 0
     */
    private String orderId;
    private String taskId;
    private String title;
    private String dep;
    private String arr;
    private String depGis;
    private String arrGis;
    private String startValidDate;
    private String endValidDate;
    private String supplierMobile;
    private int lorryId;
    private String plateNumber;
    private String carryWeight;
    private String overCarryWight;
    private String verifyCode;
    private String serviceMobile;
    private BigDecimal distance;
    private BigDecimal orderWeight;
    private BigDecimal price;
    private String unitWeight="吨";
    private String unitPrice="元";
    private String unitDistance="KM";
    private String startTime;
    private int status;

}
