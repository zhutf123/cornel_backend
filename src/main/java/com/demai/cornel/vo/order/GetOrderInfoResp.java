package com.demai.cornel.vo.order;

import com.demai.cornel.vo.task.OrderTaskLorryInfoRespBase;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2019-12-20    20:56
 * 司机侧获取订单info
 */
@Data
public class GetOrderInfoResp extends OrderTaskLorryInfoRespBase implements Serializable {

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
}
