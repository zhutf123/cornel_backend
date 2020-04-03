package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.vo.AdminGetOutStackInfo;
import com.demai.cornel.demeManager.vo.AdminGetSaleDetail;
import com.demai.cornel.demeManager.vo.AdminGetSaleList;
import com.demai.cornel.purcharse.dao.*;
import com.demai.cornel.purcharse.model.*;
import com.demai.cornel.purcharse.service.OutStackService;
import com.demai.cornel.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    10:29
 */
@Slf4j
@Service
/*管理员获取待支付订单的详情*/
public class AdminUnderPayService {
    @Resource private StackOutInfoMapper stackOutInfoMapper;
    @Resource private StoreInfoMapper storeInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private FreightInfoMapper freightInfoMapper;
    @Resource private SaleStackOutService saleStackOutService;
    @Resource private OutStackService outStackService;
    @Resource private SaleOrderMapper saleOrderMapper;

    public AdminGetSaleDetail adminGetSaleDetail(String orderId) {
        AdminGetSaleList saleOrder = saleOrderMapper.selectSaleList(orderId);
        if (saleOrder == null) {
            log.warn("adminGetSaleDetail fail due to order invalid");
            return AdminGetSaleDetail.builder().optStatus(AdminGetSaleDetail.STATUS_ENUE.ORDER_INVALID.getValue())
                    .build();
        }
        AdminGetSaleDetail adminGetSaleDetail1 = new AdminGetSaleDetail();
        BeanUtils.copyProperties(saleOrder, adminGetSaleDetail1);

        StackOutInfo stackOutInfo = stackOutInfoMapper.selectByOutId(saleOrder.getOutStackId());
        if (stackOutInfo == null) {
            stackOutInfo = outStackService.buildSystemDefaultOutStackInfo(saleOrder);
            log.debug("adminGetSaleDetail cannot find stackOutInfo from db so try to build one,build info is {}",
                    JacksonUtils.obj2String(stackOutInfo));
        }
        if (stackOutInfo == null) {
            log.debug("adminGetSaleDetail cannot find stackOutInfo from db ");
            return adminGetSaleDetail1;
        }
        StoreInfo storeInfo = storeInfoMapper.selectByStoreId(stackOutInfo.getStoreId());
        if (storeInfo == null) {
            log.debug("adminGetSaleDetail cannot find storeInfo from db ");
            return adminGetSaleDetail1;
        }

        FreightInfo freightInfo = freightInfoMapper.selectByFreightId(stackOutInfo.getFreightInfoId());
        if (freightInfo != null && freightInfo.getTransportType() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            freightInfo.getTransportType().stream().forEach(x -> {
                stringBuilder.append(TransportType.typeOf(x).getExpr());
            });
            adminGetSaleDetail1.setTransportType(stringBuilder.toString());
        }
        adminGetSaleDetail1.setStoreId(stackOutInfo.getStoreId());
        adminGetSaleDetail1.setBuyingPrice(storeInfo.getBuyingPrice());
        adminGetSaleDetail1.setCapitalCost(storeInfo.getCapitalCost());
        adminGetSaleDetail1.setFreightId(stackOutInfo.getFreightInfoId());
        adminGetSaleDetail1.setFreightPrice(stackOutInfo.getFreightPrice());
        LocationInfo loanInfo = locationInfoMapper.selectByLocationId(stackOutInfo.getFromLocation());
        adminGetSaleDetail1.setFromLocation(loanInfo == null ? "" : loanInfo.getLocation());

        List<FreightInfo> freightInfos = freightInfoMapper
                .selectFreights(stackOutInfo.getReceiveLocation(), stackOutInfo.getFromLocation());
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
            adminGetSaleDetail1.setFreightAndIncome(otherInfos);
            adminGetSaleDetail1.setShowStackInfo(1);
        }
        return adminGetSaleDetail1;
    }
}
