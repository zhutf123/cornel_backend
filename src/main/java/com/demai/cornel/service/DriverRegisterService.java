package com.demai.cornel.service;

import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.delivery.DriverCpllUserInfoReq;
import com.demai.cornel.vo.delivery.DriverCpllUserInfoResp;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author binz.zhang
 * @Date: 2020-01-17    11:10
 */
@Service @Slf4j public class DriverRegisterService {
    @Resource private UserInfoDao userInfoDao;

    public DriverCpllUserInfoResp driverCompleteUserInfo(DriverCpllUserInfoReq driverCpllUserInfoReq) {
        log.debug("driver register complete user info user info is [{}]",
                JacksonUtils.obj2String(driverCpllUserInfoReq));
        DriverCpllUserInfoResp driverCpllUserInfoResp = new DriverCpllUserInfoResp();
        Preconditions.checkNotNull(driverCpllUserInfoReq);
        if (Strings.isNullOrEmpty(driverCpllUserInfoReq.getIdCard()) || Strings
                .isNullOrEmpty(driverCpllUserInfoReq.getName()) || Strings
                .isNullOrEmpty(driverCpllUserInfoReq.getMobile()) || Strings
                .isNullOrEmpty(driverCpllUserInfoReq.getUserId())) {
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
        driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.SUCCESS.getValue());
        driverCpllUserInfoResp.setUserId(driverCpllUserInfoReq.getUserId());
        driverCpllUserInfoResp.setName(driverCpllUserInfoReq.getName());
        return driverCpllUserInfoResp;
    }

}
