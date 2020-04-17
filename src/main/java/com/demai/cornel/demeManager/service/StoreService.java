package com.demai.cornel.demeManager.service;

import com.demai.cornel.dao.*;
import com.demai.cornel.demeManager.dao.AdminUserMapper;
import com.demai.cornel.demeManager.dao.ReviewLogMapper;
import com.demai.cornel.demeManager.dao.SpecialQuoteMapper;
import com.demai.cornel.model.DryTower;
import com.demai.cornel.model.QuoteInfo;
import com.demai.cornel.purcharse.dao.LocationInfoMapper;
import com.demai.cornel.purcharse.dao.StoreInfoMapper;
import com.demai.cornel.purcharse.model.LocationInfo;
import com.demai.cornel.purcharse.model.StoreInfo;
import com.demai.cornel.service.ImgService;
import com.demai.cornel.vo.quota.ClickSystemQuoteResp;
import com.demai.cornel.vo.supplier.SupplierRegisterReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-04-17    20:33
 */
@Service @Slf4j public class StoreService {
    @Resource private SystemQuoteDao systemQuoteDao;
    @Resource private CommodityDao commodityDao;
    @Resource private QuoteInfoDao quoteInfoDao;

    @Resource private AdminUserMapper adminUserMapper;
    @Resource private DryTowerDao dryTowerDao;
    @Resource private UserInfoDao userInfoDao;
    @Resource private SpecialQuoteMapper specialQuoteMapper;
    @Resource private AdminUserLoginService adminUserLoginService;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private ImgService imgService;
    @Resource private AdminReviewService adminReviewService;
    @Resource private ReviewLogMapper reviewLogMapper;
    @Resource private StoreInfoMapper storeInfoMapper;

    public boolean convertStore(String orderId) {
        QuoteInfo quoteInfo = quoteInfoDao.selectByPrimaryKey(orderId);
        if (quoteInfo == null) {
            log.debug("convertStore fail due to quote info null");
        }
        StoreInfo storeInfo = new StoreInfo();
        storeInfo.setQuoteId(quoteInfo.getQuoteId());
        storeInfo.setBuyingPrice(quoteInfo.getQuote());
        storeInfo.setCapitalCost(new BigDecimal(30));
        storeInfo.setCommodityId(quoteInfo.getCommodityId());
        DryTower dryTower=dryTowerDao.selectByTowerId(quoteInfo.getTowerId());
        storeInfo.setLocationId(dryTower.getLocationId());
        storeInfo.setStoreKeeper(quoteInfo.getUserId());
        storeInfo.setStatus((short) 1);
        storeInfo.setUndistWeight(quoteInfo.getShipmentWeight());
        storeInfo.setWeight(quoteInfo.getShipmentWeight());
        storeInfo.setWeight(quoteInfo.getShipmentWeight());
        storeInfo.setUnitPrice(quoteInfo.getUnitPrice());
        storeInfo.setUnitWeight(quoteInfo.getUnitWeight());
        storeInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        storeInfo.setStoreId(UUID.randomUUID().toString());
        int res = storeInfoMapper.insertSelective(storeInfo);
        return res == 1;
    }
}
