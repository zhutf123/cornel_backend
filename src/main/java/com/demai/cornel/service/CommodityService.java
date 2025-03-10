package com.demai.cornel.service;

import com.demai.cornel.dao.CommodityDao;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.commodity.GetCommodityListResp;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2019-12-27    16:49
 */
@Service @Slf4j public class CommodityService {
    @Resource private CommodityDao commodityDao;

    public List<GetCommodityListResp> getAllCommodity(String userId) {
        return commodityDao.getCommdityList(userId);
    }

    public boolean insertCommodity(Commodity commodity) {
        Preconditions.checkNotNull(commodity);
        commodity.setCommodityId(UUID.randomUUID().toString());
        if (commodity.getSystemFlag() == null || commodity.getSystemFlag()
                .equals(Commodity.COMMODITY_SYSTEM_STATUS.CUSTOM.getValue())) {
            commodity.setBindUserId(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME));
        }
        return commodityDao.save(commodity) != 0;
    }

    public String insertUserDefineCommodity(Commodity commodity) {
        Preconditions.checkNotNull(commodity);
        commodity.setCommodityId(UUID.randomUUID().toString());
        commodity.setCommodityProperties(Collections.EMPTY_MAP);
        commodity.setSystemFlag(Commodity.COMMODITY_SYSTEM_STATUS.CUSTOM.getValue());
        commodity.setBindUserId(CookieAuthUtils.getCurrentUser());
        if (commodityDao.save(commodity) != 0) {
            return commodity.getCommodityId();
        }
        return null;
    }
}
