package com.demai.cornel.service;

import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.user.UserAddReq;
import com.demai.cornel.vo.user.UserAddUserResp;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-01-07    13:11
 */
@Service @Slf4j public class UserService {

    @Resource private UserInfoDao userInfoDao;

    //todo 这一块更新需要补全
    public UserAddUserResp updateUserInfo(UserAddReq userAddReq) {
        log.debug("update user info [{}]", JacksonUtils.obj2String(userAddReq));
        UserInfo updateUserInfo = new UserInfo();
        UserInfo currentUserInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
        updateUserInfo.setName(userAddReq.getUserName());
        updateUserInfo.setIdType(1);
        updateUserInfo.setIdCard(userAddReq.getIdCard());
        updateUserInfo.setRole(currentUserInfo.getRole());

        if (currentUserInfo.getMobile() == null || currentUserInfo.getMobile().size() <= 0 || !currentUserInfo
                .getMobile().contains(userAddReq.getMobile())) {
            log.debug("update user info reset mobile ");
            updateUserInfo.setMobile(Sets.newHashSet(userAddReq.getMobile()));
        }
        int optRes = userInfoDao.update(updateUserInfo);
        UserAddUserResp userAddUserResp = new UserAddUserResp();
        BeanUtils.copyProperties(userAddReq, userAddUserResp);
        if (optRes == 0) {
            log.error("update user info fail due to update db fail");
            userAddUserResp.setStatus(UserAddUserResp.CODE_ENUE.NETWORK_ERROR.getValue());
            return userAddUserResp;
        }
        userAddUserResp.setUserId(CookieAuthUtils.getCurrentUser());
        userAddUserResp.setStatus(UserAddUserResp.CODE_ENUE.SUCCESS.getValue());
        return userAddUserResp;

    }

}
