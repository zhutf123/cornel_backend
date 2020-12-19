package com.demai.cornel.service;

import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.dao.UserRoleInfoDao;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.model.UserRoleInfo;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.PhoneUtil;
import com.demai.cornel.vo.user.SupplierGetUserInfoResp;
import com.demai.cornel.vo.user.UserAddReq;
import com.demai.cornel.vo.user.UserAddUserResp;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author binz.zhang
 * @Date: 2020-01-07    13:11
 */
@Service @Slf4j public class UserService {

    @Resource private UserInfoDao userInfoDao;
    @Resource private UserRoleInfoDao userRoleInfoDao;

    //todo 这一块更新需要补全
    public UserAddUserResp updateUserInfo(UserAddReq userAddReq) {
        log.debug("update user info [{}]", JacksonUtils.obj2String(userAddReq));
        if(!Strings.isNullOrEmpty(userAddReq.getMobile()) && !PhoneUtil.isPhone(userAddReq.getMobile())){
            log.debug("update user fail due to tel illegal tel is [{}]",userAddReq.getMobile());
            return UserAddUserResp.builder().status(UserAddUserResp.CODE_ENUE.TEL_ERROR.getValue()).build();
        }
        if(!Strings.isNullOrEmpty(userAddReq.getIdCard()) && userAddReq.getIdCard().trim().length()!=18){
            log.debug("update user fail due to idcard illegal idcard is [{}]",userAddReq.getIdCard());
            return UserAddUserResp.builder().status(UserAddUserResp.CODE_ENUE.IDCARD_ERROR.getValue()).build();
        }
        UserInfo updateUserInfo = new UserInfo();
        UserInfo currentUserInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
        updateUserInfo.setName(userAddReq.getUserName());
        updateUserInfo.setIdType(1);
        updateUserInfo.setIdCard(userAddReq.getIdCard());
        updateUserInfo.setUserId(CookieAuthUtils.getCurrentUser());
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

    public SupplierGetUserInfoResp getUserInfoResp(){
        UserInfo userInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
        SupplierGetUserInfoResp supplierGetUserInfoResp = new SupplierGetUserInfoResp();
        if(userInfo==null){
            supplierGetUserInfoResp.setStatus(SupplierGetUserInfoResp.CODE_ENUE.NO_USER.getValue());
            return supplierGetUserInfoResp;
        }
        supplierGetUserInfoResp.setUserId(userInfo.getUserId());
        supplierGetUserInfoResp.setUserName(userInfo.getName());
        supplierGetUserInfoResp.setIdCard(userInfo.getIdCard());
        supplierGetUserInfoResp.setIdType(userInfo.getIdType());
        if(userInfo.getMobile()!=null && userInfo.getMobile().size()>0){
            supplierGetUserInfoResp.setMobile(userInfo.getMobile().iterator().next());
        }
        return supplierGetUserInfoResp;
    }

    /**
     * 获取当前用户的角色id
     *
     * @return
     */
    public List<String> getUserRoleId(String userId) {
        List<UserRoleInfo> userRoleInfos = userRoleInfoDao.getRolesByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleInfos)) {
            return Lists.newArrayList();
        }
        return userRoleInfos.stream().map(role -> role.getRoleId()).collect(Collectors.toList());
    }
}
