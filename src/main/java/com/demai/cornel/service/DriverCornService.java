package com.demai.cornel.service;

import com.demai.cornel.dao.LorryInfoDao;
import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.dmEnum.IdTypeEnum;
import com.demai.cornel.model.*;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.delivery.GetDriverCarCornInfoResp;
import com.demai.cornel.vo.delivery.GetDriverCornInfoResp;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    19:00
 */
@Service @Slf4j public class DriverCornService {
    @Resource private UserInfoDao userInfoDao;
    @Resource private LorryInfoDao lorryInfoDao;

    public GetDriverCornInfoResp getUserCornInfo() {
        String userId = CookieAuthUtils.getCurrentUser();
        GetDriverCornInfoResp driverCornInfo = new GetDriverCornInfoResp();

        UserInfo userInfo = userInfoDao.getUserInfoByUserId(userId);
        if (userInfo == null) {
            driverCornInfo.setOptResult(GetDriverCornInfoResp.STATUS.NO_USER.getValue());
            return driverCornInfo;
        }
        BeanUtils.copyProperties(userInfo, driverCornInfo);
        if (!userInfo.getIdType().equals(IdTypeEnum.IDCARD.getValue())) {
            driverCornInfo.setIdCard("");
        }
        if (userInfo.getMobile() != null && userInfo.getMobile().size() > 0) {
            driverCornInfo.setMobile(userInfo.getMobile().iterator().next());
        }
        driverCornInfo.setOptResult(GetDriverCornInfoResp.STATUS.SUCCESS.getValue());
        return driverCornInfo;
    }

    public List<CarCornInfo> getCarCornInfoList() {
        List<LorryInfo> lorryInfos = lorryInfoDao.getAllLorryInfoByUserId(CookieAuthUtils.getCurrentUser());
        if (lorryInfos == null) {
            return Collections.EMPTY_LIST;
        }

        List<CarCornInfo> carCornInfos = Lists.newArrayList();
        for (LorryInfo lorryInfo:lorryInfos) {
            CarCornInfo carCornInfo = new CarCornInfo();
            BeanUtils.copyProperties(lorryInfo, carCornInfo);
            carCornInfos.add(carCornInfo);
        }
        return carCornInfos;
    }

    public GetDriverCarCornInfoResp getCarCornInfo(String lorryId) {
        LorryInfo lorryInfo = lorryInfoDao.getLorryByStringLorryID(lorryId);
        GetDriverCarCornInfoResp getDriverCarCornInfoResp = new GetDriverCarCornInfoResp();
        if (lorryInfo == null) {
            getDriverCarCornInfoResp.setOptResult(GetDriverCarCornInfoResp.STATUS.NO_USER.getValue());
            return getDriverCarCornInfoResp;
        }
        BeanUtils.copyProperties(lorryInfo,getDriverCarCornInfoResp);
        getDriverCarCornInfoResp.setOptResult(GetDriverCarCornInfoResp.STATUS.SUCCESS.getValue());
        return getDriverCarCornInfoResp;
    }
    public OpCorn updateCarCornInfo(CarCornInfo carCornInfo) {
        if(carCornInfo==null){
            return OpCorn.builder().optResult(OpCorn.STATUS.PARAM_ERROR.getValue()).build();
        }
        int res = lorryInfoDao.updateCarCornInfo(carCornInfo);
        if(res==0){
            return OpCorn.builder().optResult(OpCorn.STATUS.SERVER_ERROR.getValue()).build();
        }
        return OpCorn.builder().optResult(OpCorn.STATUS.SUCCESS.getValue()).build();
    }
    public OpCorn updateUserCornInfo(DriverCornInfo driverCornInfo) {
        if(driverCornInfo==null){
            return OpCorn.builder().optResult(OpCorn.STATUS.PARAM_ERROR.getValue()).build();
        }
        UserInfo userInfoBefore = userInfoDao.getUserInfoByUserId(driverCornInfo.getUserId());
        if(userInfoBefore==null){
            return OpCorn.builder().optResult(OpCorn.STATUS.NO_OPT_TARGET.getValue()).build();
        }
        UserInfo userInfoAfter = new UserInfo();
        BeanUtils.copyProperties(driverCornInfo,userInfoAfter);
        if(!Strings.isNullOrEmpty(driverCornInfo.getMobile())){
            userInfoAfter.setMobile(Sets.newHashSet(driverCornInfo.getMobile()));
        }
        int res = userInfoDao.update(userInfoAfter);
        if(res==0){
            return OpCorn.builder().optResult(OpCorn.STATUS.SERVER_ERROR.getValue()).build();
        }
        return OpCorn.builder().optResult(OpCorn.STATUS.SUCCESS.getValue()).build();
    }

}
