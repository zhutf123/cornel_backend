package com.demai.cornel.purcharse.service;

import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.purcharse.dao.WaybillInfoMapper;
import com.demai.cornel.purcharse.model.SaleOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-22    21:20
 */
@Slf4j
@Service
public class OrderDeliverService {
    @Resource private WaybillInfoMapper waybillInfoMapper;
    @Resource private OrderInfoDao orderInfoDao;


//    private Integer getArriveCarNum(String saleId){
//        List<OrderInfo> orderInfoList = getSaleOrderDeliverInfo(saleId);
//
//        orderInfoList.forEach(x->{
//            x.getStatus().equals(OrderInfo.STATUS_ENUE.ORDER_SUCCESS )
//        });
//
//
//
//    }

    private List<OrderInfo> getSaleOrderDeliverInfo(String saleId){
        List<String> deliverOrderInfo = waybillInfoMapper.getSaleOrderDeliverId(saleId);
        if(deliverOrderInfo==null){
            return Collections.EMPTY_LIST;
        }
        List<OrderInfo>orderInfos = orderInfoDao.getOrderInfosByOrderIds(deliverOrderInfo);
        if(orderInfos!=null){
            return orderInfos;
        }
        return Collections.EMPTY_LIST;
    }




}
