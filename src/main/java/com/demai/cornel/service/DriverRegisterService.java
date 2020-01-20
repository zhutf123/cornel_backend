package com.demai.cornel.service;

import com.demai.cornel.config.CarLiceTypeConfig;
import com.demai.cornel.constant.ContextConsts;
import com.demai.cornel.dao.CarTypeInfoDao;
import com.demai.cornel.dao.ImgInfoDao;
import com.demai.cornel.dao.LorryInfoDao;
import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.dmEnum.WeightUnit;
import com.demai.cornel.model.*;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.PhoneUtil;
import com.demai.cornel.vo.delivery.DriverCpllCarReq;
import com.demai.cornel.vo.delivery.DriverCpllCarResp;
import com.demai.cornel.vo.delivery.DriverCpllUserInfoReq;
import com.demai.cornel.vo.delivery.DriverCpllUserInfoResp;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.ref.PhantomReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-01-17    11:10
 */
@Service @Slf4j public class DriverRegisterService {
    @Resource private UserInfoDao userInfoDao;
    @Resource private ImgInfoDao imgInfoDao;
    @Resource private CarTypeInfoDao carTypeInfoDao;
    @Resource private CarOptService carOptService;
    @Resource private LorryInfoDao lorryInfoDao;
    @Resource private ImgService imgService;

    /**
     * 司机完善个人信息
     *
     * @param driverCpllUserInfoReq
     * @return
     */
    public DriverCpllUserInfoResp driverCompleteUserInfo(DriverCpllUserInfoReq driverCpllUserInfoReq) {
        log.debug("driver register complete user info user info is [{}]",
                JacksonUtils.obj2String(driverCpllUserInfoReq));
        DriverCpllUserInfoResp driverCpllUserInfoResp = new DriverCpllUserInfoResp();
        Preconditions.checkNotNull(driverCpllUserInfoReq);
        if (Strings.isNullOrEmpty(driverCpllUserInfoReq.getIdCard()) || Strings
                .isNullOrEmpty(driverCpllUserInfoReq.getName()) || Strings
                .isNullOrEmpty(driverCpllUserInfoReq.getMobile()) || CollectionUtils
                .isEmpty(driverCpllUserInfoReq.getImgs())) {
            log.debug("driver register complete fail due to param lock");
            driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.PARAM_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        if(!PhoneUtil.isPhone(driverCpllUserInfoReq.getMobile())){
            log.debug("driver register complete fail due to tel format error ");
            driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.PHONE_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(driverCpllUserInfoReq, userInfo);
        userInfo.setStatus(UserInfo.USER_STATUS.ENABLE.getValue());
        userInfo.setUserId(CookieAuthUtils.getCurrentUser());
        int res = userInfoDao.update(userInfo);
        if (res == 0) {
            log.debug("driver register complete fail due to update user info fail");
            driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.SERVICE_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        if (CollectionUtils.isEmpty(driverCpllUserInfoReq.getImgs())) {
            log.debug("complete user info img is empty");
        }
        imgService.saveUserInfoImgs(driverCpllUserInfoReq.getImgs(), userInfo.getUserId());
        driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.SUCCESS.getValue());
        driverCpllUserInfoResp.setUserId(driverCpllUserInfoReq.getUserId());
        driverCpllUserInfoResp.setName(driverCpllUserInfoReq.getName());
        log.debug("complete user info [{}]", JacksonUtils.obj2String(driverCpllUserInfoResp));
        return driverCpllUserInfoResp;
    }

    /**
     * 司机完善车辆信息
     *
     * @param driverCpllCarReq
     * @return
     */
    @Transactional public DriverCpllCarResp driverCompleteCarInfo(DriverCpllCarReq driverCpllCarReq) {
        log.debug("driver register complete user info user info is [{}]", JacksonUtils.obj2String(driverCpllCarReq));
        Preconditions.checkNotNull(driverCpllCarReq);
        LorryInfo lorryInfo = new LorryInfo();
        BeanUtils.copyProperties(driverCpllCarReq, lorryInfo);
        if (!carOptService.checkAddParam(lorryInfo)) {
            log.debug("add car info error due to param lock");
            return DriverCpllCarResp.builder().optResult(DriverCpllCarResp.STATUS.PARAM_ERROR.getValue())
                    .build();
        }
        if (carOptService.getCarExistByParam(driverCpllCarReq.getPlateNumber(), driverCpllCarReq.getFrameNumber(),
                driverCpllCarReq.getEngineNo())) {
            log.debug("add car info error due to car has already exist");
            return DriverCpllCarResp.builder().optResult(DriverCpllCarResp.STATUS.CAR_EXIST.getValue())
                    .build();
        }
        UserInfo userInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
        if (userInfo == null) {
            log.debug("add car info error due to user error");
            return DriverCpllCarResp.builder().optResult(DriverCpllCarResp.STATUS.SERVICE_ERROR.getValue())
                    .build();
        }
        lorryInfo.setIdCard(userInfo.getIdCard());
        lorryInfo.setIdType(1);
        lorryInfo.setLorryId(UUID.randomUUID().toString());
        lorryInfo.setUserId(userInfo.getUserId());
        lorryInfo.setLorryId(UUID.randomUUID().toString());
        lorryInfo.setStatus(LorryInfo.STATUS_ENUE.IDLE.getValue());
        lorryInfo.setOverCarryWeight(lorryInfo.getCarryWeight().multiply(new BigDecimal(ContextConsts.LORRY_OVER_WEIGHT_FACTOR)));
        lorryInfo.setUnitWeight(WeightUnit.TONS.getExpr());
        int res = lorryInfoDao.save(lorryInfo);
        if (res == 0) {
            log.debug("add car info error due to inser lorry info into db error");
            return DriverCpllCarResp.builder().optResult(DriverCpllCarResp.STATUS.SERVICE_ERROR.getValue())
                    .build();
        }
        imgService.saveCarInfoImgs(driverCpllCarReq.getImgURL(), lorryInfo.getLorryId());
        return DriverCpllCarResp.builder().optResult(DriverCpllCarResp.STATUS.SUCCESS.getValue()).lorryId(lorryInfo.getLorryId()).build();
    }

    public List<CarTypeInfo> getCarType() {
        List<CarTypeInfo> carTypeInfos = carTypeInfoDao.selectAllCarType();
        return CollectionUtils.isEmpty(carTypeInfos) ? Collections.EMPTY_LIST : carTypeInfos;
    }

    public List<String> getCarLiceType() {
        return CarLiceTypeConfig.carLiceTypeList;
    }

}
