package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.purcharse.dao.BuyerInfoMapper;
import com.demai.cornel.purcharse.dao.OfferSheetMapper;
import com.demai.cornel.purcharse.model.OfferSheet;
import com.demai.cornel.purcharse.vo.GetSystemOfferResp;
import com.demai.cornel.util.JacksonUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-03-30    15:18
 */
@Slf4j @Service public class AdminPurchaseCornService {
    @Resource private OfferSheetMapper offerSheetMapper;
    @Resource private BuyerInfoMapper buyerInfoMapper;

    /**
     * 管理员获取系统的报价list
     *
     * @return
     */
    public List<AdminGetSysOffResp> adminGetSysOffList() {
        List<AdminGetSysOffResp> getSystemOfferReqs = offerSheetMapper.adminGetSysOfferSheet();
        return CollectionUtils.isEmpty(getSystemOfferReqs) ? Lists.newArrayList() : getSystemOfferReqs;
    }

    /**
     * 管理员获取买家list
     *
     * @param req
     * @return
     */
    public List<AdminGetBuyerResp> adminGetBuyerList(AdminGetBuyerReq req) {
        Preconditions.checkNotNull(req);
        List<AdminGetBuyerResp> buyers = buyerInfoMapper
                .adminGetBuyerList(Optional.ofNullable(req.getOffset()).orElse(0),
                        Optional.ofNullable(req.getPgSize()).orElse(10));

        return CollectionUtils.isEmpty(buyers) ? Lists.newArrayList() : buyers;
    }

    /**
     * 管理员修改买家的价格 可以设置全局的报价 也可以修改针对特定买家的价格
     *
     * @param req
     * @return
     */
    public AdminOperResp adminEditSysOfferQuote(AdminEditSysOfferReq req) {
        if (req == null) {
            return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        for (AdminEditSysOfferReq.quoteInfo x : req.getQuoteInfos()) {
            if (x.getSystemFlag() == null || !x.getSystemFlag().equals(1)) {
                if (editSpecialOfferSheet(x)) {
                    return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
                }
            } else {
                if (editSystemOfferSheet(x)) {
                    return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
                }
            }
        }
        return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.SUCCESS.getValue()).build();

    }

    /**
     * 校验系统报价的参数
     *
     * @param quoteInfo
     * @return
     */
    private boolean checkQuoteParam(AdminEditSysOfferReq.quoteInfo quoteInfo) {
        log.debug("edit system offer sheet quote  param is [{}]", JacksonUtils.obj2String(quoteInfo));
        if (quoteInfo == null) {
            log.error("edit system offer sheet quote fail due to param NULL ");
            return false;
        }
        if (Strings.isNullOrEmpty(quoteInfo.getCommodityId()) || quoteInfo.getSelfQuote() == null) {
            return false;
        }
        if (quoteInfo.getSystemFlag() != null && quoteInfo.getSystemFlag().equals(0) && Strings
                .isNullOrEmpty(quoteInfo.getUserId())) {
            return false;
        }
        return true;
    }

    /**
     * 修改系统报价
     *
     * @param quoteInfo
     * @return
     */
    @Transactional public boolean editSystemOfferSheet(AdminEditSysOfferReq.quoteInfo quoteInfo) {
        if (!checkQuoteParam(quoteInfo)) {
            return false;
        }
        OfferSheet oldOffer = offerSheetMapper.selectByCommodityId(quoteInfo.getCommodityId());
        if (oldOffer == null) {
            log.warn("edit system offer sheet fail due to old sheet not found");
            return false;
        }
        OfferSheet newOff = new OfferSheet();
        BeanUtils.copyProperties(oldOffer, newOff);
        newOff.setOfferId(UUID.randomUUID().toString());
        newOff.setCreateTime(new Timestamp(System.currentTimeMillis()));
        newOff.setPrice(quoteInfo.getSelfQuote());
        int res = offerSheetMapper.updateOfferStatusByCommodityId(newOff.getCommodityId());
        if (res != 0) {
            log.warn("edit system offer sheet fail due to update old sheet fail ");
            return false;
        }
        int resInser = offerSheetMapper.insertSelective(newOff);
        if (resInser != 0) {
            log.warn("edit system offer sheet fail due to insert new sheet fail ");
            return false;
        }
        return true;
    }

    /**
     * 修改针对个人的报价
     */
    @Transactional public boolean editSpecialOfferSheet(AdminEditSysOfferReq.quoteInfo quoteInfo) {
        if (!checkQuoteParam(quoteInfo)) {
            return false;
        }
        OfferSheet oldOffer = offerSheetMapper
                .selectSpecilaByCommodityIdAndUserId(quoteInfo.getCommodityId(), quoteInfo.getUserId());
        boolean alreadyExist = false;
        if (oldOffer == null) {
            oldOffer = offerSheetMapper.selectByCommodityId(quoteInfo.getCommodityId());
        } else {
            alreadyExist = true;
        }
        if (oldOffer == null) {
            log.warn("edit special offer sheet fail due to old sheet not found");
            return false;
        }
        OfferSheet newOff = new OfferSheet();
        BeanUtils.copyProperties(oldOffer, newOff);
        newOff.setOfferId(UUID.randomUUID().toString());
        newOff.setCreateTime(new Timestamp(System.currentTimeMillis()));
        newOff.setTargetUserId(quoteInfo.getUserId());
        newOff.setPrice(quoteInfo.getSelfQuote());
        if (alreadyExist) {
            int res = offerSheetMapper
                    .updateOfferStatusByCommodityIdAndUserId(newOff.getCommodityId(), newOff.getTargetUserId());
            if (res != 0) {
                log.warn("edit special offer sheet fail due to update old sheet fail ");
                return false;
            }
            int resInser = offerSheetMapper.insertSelective(newOff);
            if (resInser != 0) {
                log.warn("edit special offer sheet fail due to insert new sheet fail ");
                return false;
            }
            return true;
        }
        int resInser = offerSheetMapper.insertSelective(newOff);
        if (resInser != 0) {
            log.warn("edit special offer sheet fail due to insert new sheet fail ");
            return false;
        }
        return true;
    }
}
