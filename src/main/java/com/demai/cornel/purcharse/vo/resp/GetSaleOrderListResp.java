package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.model.Commodity;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author binz.zhang
 * @Date: 2020-02-19    13:59
 */
@Data
public class GetSaleOrderListResp {
    private Commodity commodity; //商品属性
    private BigDecimal weight;   // 重量
    private String unitWeight;  //重量单位
    private BigDecimal commodityPrice;//商品单价
    private BigDecimal freightPrice;//运费
    private BigDecimal orderPrice;//订单总价值
    private String unitPrice;// 价格单位
    private Timestamp receiveStartTime;//收货开始时间
    private Timestamp receiveEndTime;//收货结束时间
    private Integer arriveCar;//到达车辆
    private Integer unarriveCar;// 未达到车辆
    private Integer status;

}
