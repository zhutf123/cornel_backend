package com.demai.cornel.purcharse.service;

import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.model.DriverCornInfo;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.purcharse.dao.WaybillInfoMapper;
import com.demai.cornel.purcharse.model.SaleOrder;
import com.demai.cornel.purcharse.vo.resp.DriverInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-22    21:20
 */
@Slf4j @Service public class OrderDeliverService {
    @Resource private WaybillInfoMapper waybillInfoMapper;
    @Resource private OrderInfoDao orderInfoDao;

    @Resource private UserInfoDao userInfoDao;

    public List<DriverInfoResp>  getSaleCarStatus(String saleId) {
        List<OrderInfo> orderInfoList = getSaleOrderDeliverInfo(saleId);
        List<DriverInfoResp> driverSimpInfo = new ArrayList<>();
        orderInfoList.forEach(x -> {
            DriverInfoResp driverInfoResp = new DriverInfoResp();
            BeanUtils.copyProperties(x, driverInfoResp);
            UserInfo userInfo = userInfoDao.getUserInfoByUserId(x.getUserId());
            if (userInfo != null) {
                driverInfoResp.setMobile(userInfo.getMobile().iterator().next());
                driverInfoResp.setName(userInfo.getName());
            }
            driverSimpInfo.add(driverInfoResp);

        });
        return driverSimpInfo;
    }

    private List<OrderInfo> getSaleOrderDeliverInfo(String saleId) {
        List<String> deliverOrderInfo = waybillInfoMapper.getSaleOrderDeliverId(saleId);
        if (CollectionUtils.isEmpty(deliverOrderInfo)) {
            return Collections.EMPTY_LIST;
        }
        List<OrderInfo> orderInfos = orderInfoDao.getOrderInfosByOrderIds(deliverOrderInfo);
        if (orderInfos != null) {
            return orderInfos;
        }
        return Collections.EMPTY_LIST;
    }

}
