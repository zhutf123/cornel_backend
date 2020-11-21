package com.demai.cornel.purcharse.service;

import com.demai.cornel.purcharse.dao.FreightInfoMapper;
import com.demai.cornel.purcharse.dao.LocationInfoMapper;
import com.demai.cornel.purcharse.model.FreightInfo;
import com.demai.cornel.purcharse.model.LocationInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    14:28
 */
@Service @Slf4j public class LocationService {
    @Resource private FreightInfoMapper freightInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;

    /**
     * 获取最价格最低的运输方式及路径
     *
     * @param fromLocation
     * @param toLocation
     * @return
     */
    public FreightInfo getPriceOptimum(String fromLocationId, String toLocationId) {
        LocationInfo fromLocationInfo = locationInfoMapper.selectByLocationId(fromLocationId);
        LocationInfo toLocationInfo = locationInfoMapper.selectByLocationId(toLocationId);
        if (fromLocationInfo == null || toLocationInfo == null) {
            return null;
        }
        return freightInfoMapper
                .selectMinPriceRoute(fromLocationInfo.getLocationArea(), toLocationInfo.getLocationArea());
    }

    public List<FreightInfo> getAllFreightInfos(String fromLocationId, String toLocationId) {
        LocationInfo fromLocationInfo = locationInfoMapper.selectByLocationId(fromLocationId);
        LocationInfo toLocationInfo = locationInfoMapper.selectByLocationId(toLocationId);
        if (fromLocationInfo == null || toLocationInfo == null) {
            return Collections.EMPTY_LIST;
        }
        return freightInfoMapper
                .selectFreightsByLocationArea(fromLocationInfo.getLocationArea(), toLocationInfo.getLocationArea());

    }

}
