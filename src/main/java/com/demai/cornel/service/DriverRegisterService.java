package com.demai.cornel.service;

import com.demai.cornel.config.CarLiceTypeConfig;
import com.demai.cornel.dao.CarTypeInfoDao;
import com.demai.cornel.dao.ImgInfoDao;
import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.model.CarTypeInfo;
import com.demai.cornel.model.ImgInfo;
import com.demai.cornel.model.ImgInfoReq;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.delivery.DriverCpllUserInfoReq;
import com.demai.cornel.vo.delivery.DriverCpllUserInfoResp;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(CookieAuthUtils.getCurrentUser());
        BeanUtils.copyProperties(driverCpllUserInfoReq, userInfo);
        int res = userInfoDao.update(userInfo);
        if (res == 0) {
            log.debug("driver register complete fail due to update user info fail");
            driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.SERVICE_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        if (CollectionUtils.isEmpty(driverCpllUserInfoReq.getImgs())) {
            log.debug("complete user info img is empty");
        }
        driverCpllUserInfoReq.getImgs().stream().forEach(x -> {
            ImgInfo imgInfo = new ImgInfo();
            imgInfo.setImgId(UUID.randomUUID().toString());
            log.debug("img key is [{}] url is [{}]", x.getKey(), x.getUrl());
            //todo imgdesc 为什么是null
            ImgInfo.IMGDESC imgdesc = ImgInfo.IMGDESC.keyOf(x.getKey());
            log.debug("img desc is [{}]", JacksonUtils.obj2String(imgdesc));
            if (imgdesc != null) {
                imgInfo.setImgDesc(imgdesc.getExpr());
            }
            imgInfo.setUrl(x.getUrl());
            imgInfo.setBindId(CookieAuthUtils.getCurrentUser());
            imgInfo.setStatus(ImgInfo.STATUS.EXIST.getValue());
            imgInfo.setBindType(ImgInfo.BINDTYPESTATUS.BIND_USER.getValue());
            imgInfoDao.insert(imgInfo);
        });
        driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.SUCCESS.getValue());
        driverCpllUserInfoResp.setUserId(driverCpllUserInfoReq.getUserId());
        driverCpllUserInfoResp.setName(driverCpllUserInfoReq.getName());
        log.debug("complete user info [{}]", JacksonUtils.obj2String(driverCpllUserInfoResp));
        return driverCpllUserInfoResp;
    }

   public List<CarTypeInfo> getCarType() {
        List<CarTypeInfo> carTypeInfos = carTypeInfoDao.selectAllCarType();
        return CollectionUtils.isEmpty(carTypeInfos) ? Collections.EMPTY_LIST : carTypeInfos;
    }
    public List<String> getCarLiceType() {
        return CarLiceTypeConfig.carLiceTypeList;
    }
}
