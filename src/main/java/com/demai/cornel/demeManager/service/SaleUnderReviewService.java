package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.purcharse.dao.*;
import com.demai.cornel.purcharse.model.*;
import com.demai.cornel.purcharse.service.LocationService;
import com.demai.cornel.purcharse.service.OutStackService;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    10:07
 */
/*管理员获取到待审核订单的信息*/
@Service @Slf4j public class SaleUnderReviewService {
    @Resource private SaleOrderMapper saleOrderMapper;
    @Resource private StackOutInfoMapper stackOutInfoMapper;
    @Resource private StoreInfoMapper storeInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private FreightInfoMapper freightInfoMapper;
    @Resource private SaleStackOutService saleStackOutService;
    @Resource private OutStackService outStackService;
    @Resource private LocationService locationService;

    public JsonResult adminGetSaleList(Integer status, Integer offset, Integer pgSize) {
        List<AdminUnRevSaleList> saleOrder = saleOrderMapper
                .AdminGetUnRevSaleOrderList(status, Optional.ofNullable(pgSize).orElse(10),
                        Optional.ofNullable(offset).orElse(0));
        if (saleOrder == null) {
            log.warn("SaleUnderReviewService--adminGetSaleList fail due to order invalid");
            return JsonResult.success(Collections.EMPTY_LIST);
        }
        return JsonResult.success(saleOrder);
    }

    public JsonResult adminGetSaleDetail(String orderId) {
        AdminUnRevSaleDetail saleOrder = saleOrderMapper.adminGetUnRevSaleDetail(orderId);
        if (saleOrder == null) {
            log.warn("adminGetSaleDetail fail due to order invalid");
            return JsonResult.success(
                    AdminGetSaleDetail.builder().optStatus(AdminGetSaleDetail.STATUS_ENUE.ORDER_INVALID.getValue())
                            .build());
        }

        StackOutInfo stackOutInfo = stackOutInfoMapper.selectByOutId(saleOrder.getOutStackId());
        if (stackOutInfo == null) {
            stackOutInfo = outStackService.buildSystemDefaultOutStackInfo(saleOrder, false);
            log.debug("adminGetSaleDetail cannot find stackOutInfo from db so try to build one,build info is {}",
                    JacksonUtils.obj2String(stackOutInfo));
        }
        if (stackOutInfo == null) {
            log.debug("adminGetSaleDetail cannot find stackOutInfo from db ");
            return JsonResult.success(saleOrder);
        }
        StoreInfo storeInfo = storeInfoMapper.selectByStoreId(stackOutInfo.getStoreId());
        if (storeInfo == null) {
            log.debug("adminGetSaleDetail cannot find storeInfo from db ");
            return JsonResult.success(saleOrder);
        }

        FreightInfo freightInfo = freightInfoMapper.selectByFreightId(stackOutInfo.getFreightInfoId());
        if (freightInfo != null && freightInfo.getTransportType() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            freightInfo.getTransportType().stream().forEach(x -> {
                stringBuilder.append(TransportType.typeOf(x).getExpr());
            });
            saleOrder.setTransportType(stringBuilder.toString());
        }
        saleOrder.setStoreId(stackOutInfo.getStoreId());
        saleOrder.setBuyingPrice(storeInfo.getBuyingPrice());
        saleOrder.setCapitalCost(storeInfo.getCapitalCost());
        saleOrder.setFreightId(stackOutInfo.getFreightInfoId());
        saleOrder.setFreightPrice(stackOutInfo.getFreightPrice());
        LocationInfo loanInfo = locationInfoMapper.selectByLocationId(stackOutInfo.getFromLocation());
        saleOrder.setFromLocation(loanInfo == null ? "" : loanInfo.getLocation());

        List<FreightInfo> freightInfos = locationService
                .getAllFreightInfos(stackOutInfo.getReceiveLocation(), stackOutInfo.getFromLocation());
        if (freightInfos != null) {
            List<AdminGetOutStackInfo.OtherInfo> otherInfos = new ArrayList<>(freightInfos.size());
            freightInfos.stream().forEach(fiT -> {
                AdminGetOutStackInfo.OtherInfo otherInfo = new AdminGetOutStackInfo.OtherInfo();
                otherInfo.setFreightId(fiT.getFreightId());
                otherInfo.setFreightPrice(fiT.getPrice());
                if (fiT != null && fiT.getTransportType() != null) {
                    StringBuilder stringBuilder = new StringBuilder("");
                    fiT.getTransportType().stream().forEach(xT -> {
                        stringBuilder.append(TransportType.typeOf(xT).getExpr()).append("+");
                    });
                    otherInfo.setTransportType(stringBuilder.toString().substring(0, stringBuilder.lastIndexOf("+")));
                }
                otherInfo.setInCome(
                        saleOrder.getCommodityPrice().subtract(fiT.getPrice()).subtract(storeInfo.getBuyingPrice()));
                otherInfos.add(otherInfo);
            });
            saleOrder.setFreightAndIncome(otherInfos);
            saleOrder.setShowStackInfo(1);
        }
        return JsonResult.success(saleOrder);
    }
}
