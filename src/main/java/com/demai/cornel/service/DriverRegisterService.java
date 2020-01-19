package com.demai.cornel.service;

import com.demai.cornel.dao.ImgInfoDao;
import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.model.ImgInfo;
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
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-01-17    11:10
 */
@Service @Slf4j public class DriverRegisterService {
    @Resource private UserInfoDao userInfoDao;
    @Resource private ImgInfoDao imgInfoDao;

    public DriverCpllUserInfoResp driverCompleteUserInfo(DriverCpllUserInfoReq driverCpllUserInfoReq) {
        log.debug("driver register complete user info user info is [{}]",
                JacksonUtils.obj2String(driverCpllUserInfoReq));
        DriverCpllUserInfoResp driverCpllUserInfoResp = new DriverCpllUserInfoResp();
        Preconditions.checkNotNull(driverCpllUserInfoReq);
        if (Strings.isNullOrEmpty(driverCpllUserInfoReq.getIdCard()) || Strings
                .isNullOrEmpty(driverCpllUserInfoReq.getName()) || Strings
                .isNullOrEmpty(driverCpllUserInfoReq.getMobile()) || Strings
                .isNullOrEmpty(driverCpllUserInfoReq.getUserId()) || CollectionUtils
                .isEmpty(driverCpllUserInfoReq.getImgs())) {
            log.debug("driver register complete fail due to param lock");
            driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.PARAM_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(driverCpllUserInfoReq, userInfo);
        int res = userInfoDao.update(userInfo);
        if (res == 0) {
            log.debug("driver register complete fail due to update user info fail");
            driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.SERVICE_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        if(CollectionUtils.isEmpty(driverCpllUserInfoReq.getImgs())){
            log.debug("complete user info img is empty");
        }
        driverCpllUserInfoReq.getImgs().stream().forEach(x -> {
            ImgInfo imgInfo = new ImgInfo();
            imgInfo.setImgId(UUID.randomUUID().toString());
            log.debug("img key is [{}] url is [{}]",x.getKey(),x.getUrl());
           // imgInfo.setImgDesc(ImgInfo.IMGDESC.keyOf(x.getKey()).getExpr());
            log.debug("img key 2",x.getKey(),x.getUrl());
            imgInfo.setUrl(x.getUrl());
            log.debug("img key 3",x.getKey(),x.getUrl());

            imgInfo.setBindId(CookieAuthUtils.getCurrentUser());
            log.debug("img key 4",x.getKey(),x.getUrl());

            imgInfo.setBindType(ImgInfo.BINDTYPESTATUS.BIND_USER.getValue());
            log.debug("img key 5 ",x.getKey(),x.getUrl());

            imgInfoDao.insert(imgInfo);
        });
        driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.SUCCESS.getValue());
        driverCpllUserInfoResp.setUserId(driverCpllUserInfoReq.getUserId());
        driverCpllUserInfoResp.setName(driverCpllUserInfoReq.getName());
        return driverCpllUserInfoResp;
    }



}
