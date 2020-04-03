package com.demai.cornel.purcharse.service;

import com.demai.cornel.demeManager.vo.AdminGetSaleList;
import com.demai.cornel.demeManager.vo.AdminUnRevSaleDetail;
import com.demai.cornel.purcharse.dao.FreightInfoMapper;
import com.demai.cornel.purcharse.dao.StackOutInfoMapper;
import com.demai.cornel.purcharse.dao.StoreInfoMapper;
import com.demai.cornel.purcharse.model.FreightInfo;
import com.demai.cornel.purcharse.model.SaleOrder;
import com.demai.cornel.purcharse.model.StackOutInfo;
import com.demai.cornel.purcharse.model.StoreInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    13:11
 */
@Slf4j @Service public class OutStackService {
    @Resource private StoreInfoMapper storeInfoMapper;
    @Resource private FreightInfoMapper freightInfoMapper;
    @Resource private LocationService locationService;
    @Resource private StackOutInfoMapper stackOutInfoMapper;

    public StackOutInfo buildSystemDefaultOutStackInfo(AdminUnRevSaleDetail saleOrder) {
        List<StoreInfo> storeInfos = storeInfoMapper
                .selectStoreIdByCommodityIdAndWeight(saleOrder.getCommodityId(), saleOrder.getWeight());
        if (storeInfos == null || storeInfos.size() == 0) {
            return null;
        }
        BigDecimal minTotalPrice = null;
        StoreInfo optimumStoreId = null;
        FreightInfo optimumFreight = null;
        for (StoreInfo storeInfo : storeInfos) {
            FreightInfo freightInfo = locationService
                    .getPriceOptimum(storeInfo.getLocationId(), saleOrder.getReceiveLocation());
            if (freightInfo != null && freightInfo.getPrice() != null) {
                BigDecimal totalPrice = storeInfo.getBuyingPrice().add(freightInfo.getPrice())
                        .add(storeInfo.getCapitalCost());
                if (minTotalPrice == null) {
                    minTotalPrice = totalPrice;
                    optimumStoreId = storeInfo;
                    optimumFreight = freightInfo;
                } else {
                    minTotalPrice = minTotalPrice.compareTo(totalPrice) == -1 ? totalPrice : minTotalPrice;
                    optimumStoreId = storeInfo;
                    optimumFreight = freightInfo;
                }
            }
        }
        if (optimumStoreId == null) {
            log.warn("buildSystemDefaultOutStackInfo fail due to can not find the store");
            return null;
        }
        StackOutInfo stackOutInfo = new StackOutInfo();
        stackOutInfo.setStoreId(optimumStoreId.getStoreId());
        stackOutInfo.setOutId(UUID.randomUUID().toString());
        stackOutInfo.setBuyerId(saleOrder.getBuyerId());
        stackOutInfo.setCommodityId(saleOrder.getCommodityId());
        stackOutInfo.setFromLocation(optimumStoreId.getLocationId());
        stackOutInfo.setReceiveLocation(saleOrder.getReceiveLocation());
        stackOutInfo.setFreightPrice(optimumFreight.getPrice());
        stackOutInfo.setFreightInfoId(optimumFreight.getFreightId());
        stackOutInfo.setOrderPrice(saleOrder.getOrderPrice());
        stackOutInfo.setUnitPrice(saleOrder.getUnitPrice());
        stackOutInfo.setUnitWeight(saleOrder.getUnitWeight());
        int res = stackOutInfoMapper.insertSelective(stackOutInfo);
        if (res != 1) {
            log.warn("buildSystemDefaultOutStackInfo fail due to insert stackOutInfo fail ");
            return null;
        }
        saleOrder.setOutStackId(stackOutInfo.getOutId());
        saleOrder.setEsInCome(saleOrder.getCommodityPrice().subtract(minTotalPrice));
        return stackOutInfo;
    }

//    public StackOutInfo buildSystemDefaultOutStackInfo(AdminUnRevSaleDetail saleOrder) {
//        SaleOrder saleOrder1 = new SaleOrder();
//        BeanUtils.copyProperties(saleOrder, saleOrder1);
//        saleOrder1.setReceiveLocation(saleOrder.getReceiveLocationId());
//        StackOutInfo stackOutInfo = buildSystemDefaultOutStackInfo(saleOrder1);
//        if(stackOutInfo!=null){
//            saleOrder.setEsInCome(saleOrder1.getEsIncome());
//            saleOrder.get(saleOrder1.getEsIncome());
//        }
//    }
}
