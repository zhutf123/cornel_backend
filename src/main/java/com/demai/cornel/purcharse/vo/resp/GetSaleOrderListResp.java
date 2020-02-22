package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.model.Commodity;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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
    private String receiveStartTime;//收货开始时间
    private String receiveEndTime;//收货结束时间
    private Integer carTotalNum;//车辆总数
    private List<DriverInfoResp> carInfo;// 车辆详情
    private Integer status;
    private String commodityId;
    private String orderId;

}
