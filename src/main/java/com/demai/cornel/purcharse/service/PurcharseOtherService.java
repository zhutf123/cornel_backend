package com.demai.cornel.purcharse.service;

import com.demai.cornel.config.ServiceMobileConfig;
import com.demai.cornel.constant.ContextConsts;
import com.demai.cornel.dao.CommodityDao;
import com.demai.cornel.purcharse.dao.BuyerInfoMapper;
import com.demai.cornel.purcharse.dao.LocationInfoMapper;
import com.demai.cornel.purcharse.dao.StoreInfoMapper;
import com.demai.cornel.purcharse.model.BuyerInfo;
import com.demai.cornel.purcharse.model.LocationInfo;
import com.demai.cornel.purcharse.model.StoreInfo;
import com.demai.cornel.purcharse.vo.req.AddLocationReq;
import com.demai.cornel.purcharse.vo.resp.*;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.DateFormatUtils;
import com.demai.cornel.vo.JsonResult;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Author binz.zhang
 * @Date: 2020-02-14    20:35
 */
@Slf4j @Service public class PurcharseOtherService {

    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private BuyerInfoMapper buyerInfoMapper;
    @Resource private CommodityDao commodityDao;
    @Resource private StoreInfoMapper storeInfoMapper;

    public JsonResult addLocation(AddLocationReq addLocationReq) {

        if (addLocationReq == null || Strings.isNullOrEmpty(addLocationReq.getLocation()) || Strings
                .isNullOrEmpty(addLocationReq.getLocationDetail()) || Strings
                .isNullOrEmpty(addLocationReq.getLocationArea())) {
            log.debug("addLocation fail due to param error");
            return JsonResult.success(
                    OptLocationResp.builder().optStatus(OptLocationResp.STATUS_ENUE.PARAM_ERROR.getValue()).build());
        }

        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserId(CookieAuthUtils.getCurrentUser());
        if (buyerInfo == null) {
            log.debug("addLocation fail due to user  error");
            return JsonResult.success(
                    OptLocationResp.builder().optStatus(OptLocationResp.STATUS_ENUE.USER_ERROR.getValue()).build());
        }
        LocationInfo locationInfo = locationInfoMapper
                .selectLocationIdByInfo(addLocationReq.getLocationArea(), addLocationReq.getLocationDetail(),
                        addLocationReq.getLocation());
        if (locationInfo != null) {
            if (addLocationReq.getDefaultFlag() != null && addLocationReq.getLocation().equals(1)) {
                buyerInfoMapper
                        .updateFrequentlyLocation(CookieAuthUtils.getCurrentUser(), null, locationInfo.getLocationId());
            }
            return JsonResult.success(
                    OptLocationResp.builder().optStatus(OptLocationResp.STATUS_ENUE.SUCCESS.getValue())
                            .locationId(locationInfo.getLocationId()).build());
        }
        LocationInfo insertLc = new LocationInfo();
        BeanUtils.copyProperties(addLocationReq, insertLc);
        insertLc.setLocationId(UUID.randomUUID().toString());
        insertLc.setStatus(1);
        int resInLo = locationInfoMapper.insertSelective(insertLc);
        if (resInLo != 1) {
            log.debug("addLocation fail due to db error");
            return JsonResult.success(
                    OptLocationResp.builder().optStatus(OptLocationResp.STATUS_ENUE.SERVER_ERROR.getValue()).build());

        }
        Set<String> freLocation = buyerInfo.getFrequentlyLocation();
        if (freLocation == null) {
            freLocation = Sets.newHashSet();
        }
        freLocation.add(insertLc.getLocationId());
        if (addLocationReq.getDefaultFlag() != null && addLocationReq.getDefaultFlag().equals(1)) {
            buyerInfoMapper
                    .updateFrequentlyLocation(CookieAuthUtils.getCurrentUser(), freLocation, insertLc.getLocationId());
            return JsonResult.success(
                    OptLocationResp.builder().optStatus(OptLocationResp.STATUS_ENUE.SUCCESS.getValue())
                            .locationId(insertLc.getLocationId()).build());
        }
        buyerInfoMapper.updateFrequentlyLocation(CookieAuthUtils.getCurrentUser(), freLocation, null);
        return JsonResult.success(OptLocationResp.builder().optStatus(OptLocationResp.STATUS_ENUE.SUCCESS.getValue())
                .locationId(insertLc.getLocationId()).build());
    }

    public JsonResult getLocationList() {
        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserId(CookieAuthUtils.getCurrentUser());
        if (buyerInfo == null) {
            return JsonResult.success(
                    GetLocationResp.builder().status(GetLocationResp.STATUS_ENUE.USER_ERROR.getValue()).build());
        }
        if (buyerInfo.getFrequentlyLocation() == null) {
            return JsonResult.success(GetLocationResp.builder().status(GetLocationResp.STATUS_ENUE.SUCCESS.getValue())
                    .locations(Collections.EMPTY_LIST).build());
        }
        List<LocationInfo> locationInfos = locationInfoMapper
                .getLocationInfoByLocationId(buyerInfo.getFrequentlyLocation());
        if (locationInfos == null) {
            return JsonResult.success(GetLocationResp.builder().status(GetLocationResp.STATUS_ENUE.SUCCESS.getValue())
                    .locations(Collections.EMPTY_LIST).build());
        }
        List<GetLocationResp.LocationEntiy> locationEntiys = new LinkedList<>();
        locationInfos.stream().forEach(x -> {
            GetLocationResp.LocationEntiy locationEntiy = new GetLocationResp.LocationEntiy();
            BeanUtils.copyProperties(x, locationEntiy);
            if (!Strings.isNullOrEmpty(buyerInfo.getDefaultLocation()) && buyerInfo.getDefaultLocation()
                    .equals(x.getLocationId())) {
                locationEntiy.setDefaultFlag(1);
            }
            locationEntiys.add(locationEntiy);
        });
        return JsonResult.success(GetLocationResp.builder().status(GetLocationResp.STATUS_ENUE.SUCCESS.getValue())
                .locations(locationEntiys).build());
    }

    public GetLocationDetailResp getLocationDetail(String locationId) {
        LocationInfo locationInfo = locationInfoMapper.selectByLocationId(locationId);
        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserId(CookieAuthUtils.getCurrentUser());
        if (locationInfo == null) {
            return GetLocationDetailResp.builder()
                    .optStatus(GetLocationDetailResp.STATUS_ENUE.LOCATION_ERROR.getValue()).build();
        }
        GetLocationDetailResp getLocationDetailResp = new GetLocationDetailResp();
        BeanUtils.copyProperties(locationInfo,getLocationDetailResp);
        getLocationDetailResp.setOptStatus(GetLocationDetailResp.STATUS_ENUE.SUCCESS.getValue());
        if(buyerInfo!=null&&buyerInfo.getDefaultLocation().equals(locationId)){
            getLocationDetailResp.setDefaultFlag(1);
        }else {
            getLocationDetailResp.setDefaultFlag(0);
        }
        return getLocationDetailResp;
    }


    public List<BuyerCommitListResp>getCommodityList(){
        List<BuyerCommitListResp> buyerCommitListResps = commodityDao.buyerGetCommodityList(CookieAuthUtils.getCurrentUser());
        if(buyerCommitListResps==null){
            return Collections.EMPTY_LIST;
        }
        buyerCommitListResps.stream().forEach(x->{
            List<StoreInfo> storeInfo = storeInfoMapper.selectStoreIdByCommodityId(x.getCommodityId());
            if(storeInfo==null||storeInfo.size()==0){
                x.setReceiveStartTime(DateFormatUtils.getAfterTime(System.currentTimeMillis(), DateFormatUtils.ISO_DATE_PATTERN, 10));
                x.setReceiveEndTime(DateFormatUtils.getAfterTime(System.currentTimeMillis(), DateFormatUtils.ISO_DATE_PATTERN, 15));
            }else {
                x.setReceiveStartTime(DateFormatUtils.getAfterTime(System.currentTimeMillis(), DateFormatUtils.ISO_DATE_PATTERN, 5));
                x.setReceiveEndTime(DateFormatUtils.getAfterTime(System.currentTimeMillis(), DateFormatUtils.ISO_DATE_PATTERN, 10));
            }
        });
        return buyerCommitListResps;

    }

    public JsonResult editLocation(AddLocationReq req) {
        if (req == null || Strings.isNullOrEmpty(req.getLocation()) || Strings.isNullOrEmpty(req.getLocationDetail())
                || Strings.isNullOrEmpty(req.getLocationArea())) {
            log.debug("edit Location fail due to param error");
            return JsonResult.success(
                    OptLocationResp.builder().optStatus(OptLocationResp.STATUS_ENUE.PARAM_ERROR.getValue()).build());
        }
        LocationInfo locationInfo = new LocationInfo();
        BeanUtils.copyProperties(req, locationInfo);
        int updateReq = locationInfoMapper.updateByPrimaryKeySelective(locationInfo);
        if (updateReq != 1) {
            return JsonResult.success(
                    OptLocationResp.builder().optStatus(OptLocationResp.STATUS_ENUE.SERVER_ERROR.getValue()).build());
        }
        if (req.getDefaultFlag() != null && req.getDefaultFlag().equals(1)) {
            buyerInfoMapper.updateDefaultLocation(CookieAuthUtils.getCurrentUser(), req.getLocationId());
        }
        return JsonResult.success(OptLocationResp.builder().optStatus(OptLocationResp.STATUS_ENUE.SUCCESS.getValue())
                .locationId(req.getLocationId()).build());
    }



    public GetServiceMobileResp getServiceMobile(){
        return GetServiceMobileResp.builder().mobile(ServiceMobileConfig.serviceMobile.iterator().next()).build();
    }

}
