package com.demai.cornel.auth.service.impl;

import com.demai.cornel.auth.service.IUserService;
import com.demai.cornel.auth.dao.AclDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Resource
    private AclDao aclDao;


    @Override
    public List<String> getUserAuthCode(String userId) {
        List<String> acls = aclDao.selectAclsByUserId(userId);
        return CollectionUtils.isEmpty(acls) ? Collections.EMPTY_LIST : acls;
    }

    @Override
    public String getUserRoleByUserId(String userId) {
        return null;
    }

    @Override
    public String getUserIdByTel(String tel) {
        return null;
    }

    @Override
    public String getUserIdByOpenId(String openId) {
        return null;
    }
}
