package com.demai.cornel.purcharse.service;

import com.demai.cornel.purcharse.dao.BuyerInfoMapper;
import com.demai.cornel.purcharse.dao.LocationInfoMapper;
import com.demai.cornel.purcharse.dao.OfferSheetMapper;
import com.demai.cornel.purcharse.model.BuyerInfo;
import com.demai.cornel.purcharse.model.OfferSheet;
import com.demai.cornel.purcharse.vo.GetSystemOfferResp;
import com.demai.cornel.purcharse.vo.req.GetSystemOfferReq;
import com.demai.cornel.purcharse.vo.resp.ClickSystemOfferResp;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.quota.ClickSystemQuoteResp;
import com.demai.cornel.vo.quota.GerQuoteListResp;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    14:18
 */
@Slf4j @Service public class PurchaseCornService {
    @Resource private OfferSheetMapper offerSheetMapper;
    @Resource private BuyerInfoMapper buyerInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;

    /**
     * 获取当前系统报价的接口
     *
     * @param getSystemOfferReq
     * @return
     */
    public List<GetSystemOfferResp> getSystemOfferRespList(GetSystemOfferReq getSystemOfferReq) {
        Preconditions.checkNotNull(getSystemOfferReq);
        List<GetSystemOfferResp> getSystemOfferReqs = offerSheetMapper
                .getSystemOfferSheet(getSystemOfferReq.getOfferId(),
                        Optional.ofNullable(getSystemOfferReq.getPgSize()).orElse(10));
        buildSystemQuoteDetail(getSystemOfferReqs);
        return CollectionUtils.isEmpty(getSystemOfferReqs) ? Lists.newArrayList() : getSystemOfferReqs;

    }

    /**
     * 构建系统报价的报价详情数据
     *
     * @param quoteListResps
     * @return
     */
    void buildSystemQuoteDetail(List<GetSystemOfferResp> quoteListResps) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(quoteListResps)) {
            return;
        }
        quoteListResps.stream().forEach(x -> {
            List<GetSystemOfferResp.Detail> details = new LinkedList<>();
            details.add(new GetSystemOfferResp.Detail("质量标准", GerQuoteListResp.convertProperties(x.getProperties())));
            details.add(new GetSystemOfferResp.Detail("单价", x.getPrice().toString() + " /" + x.getUnitPrice()));
            details.add(new GetSystemOfferResp.Detail("注意事项", x.getNotice()));
            x.setDetail(details);
        });
    }

    public ClickSystemOfferResp clickSystemQuoteResp(String offerId) {
        if (Strings.isNullOrEmpty(offerId)) {
            return ClickSystemOfferResp.builder().status(ClickSystemQuoteResp.STATUS_ENUE.NO_OFFER.getValue()).build();
        }
        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserId(CookieAuthUtils.getCurrentUser());
        if (buyerInfo == null) {
            log.debug("clickSystemQuoteResp fail due to user ");
            return ClickSystemOfferResp.builder().status(ClickSystemQuoteResp.STATUS_ENUE.USER_ERROR.getValue())
                    .build();
        }
        ClickSystemOfferResp clickSystemOfferResp = ClickSystemOfferResp.builder().userName(buyerInfo.getUserName())
                .build();

        List<String> location = new ArrayList<>();
        if (!CollectionUtils.isEmpty(buyerInfo.getFrequentlyLocation())) {
            List<String> locationTemp = locationInfoMapper.getLocationByLocationId(buyerInfo.getFrequentlyLocation());
            if(locationTemp!=null){
                location = locationTemp;
            }
        }
        clickSystemOfferResp.setReceiveLocation(location);
        if(!CollectionUtils.isEmpty(buyerInfo.getMobile())){
            clickSystemOfferResp.setMobile(buyerInfo.getMobile().iterator().next());
        }
        OfferSheet offerSheet = offerSheetMapper.selectByOfferId(offerId);
        //todo 加入抢单逻辑
        return null;

    }
}
