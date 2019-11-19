package com.demai.cornel.auth.service.impl;

import com.demai.cornel.auth.dao.AclDao;
import com.demai.cornel.auth.service.IUrlAclService;
import com.demai.cornel.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UrlAclServiceImpl implements IUrlAclService {
    @Resource
    private AclDao aclDao;

    @Override
    public List<String> getURLAcls(String url) {
        List<String> urlAcls = aclDao.selectAclsByUrl(url);
        return CollectionUtils.isEmpty(urlAcls) ? Collections.EMPTY_LIST : urlAcls;
    }

    @Override
    public boolean checkUserUrlAcls(String userId, String url) {
        if (StringUtil.isEmpty(userId)) {
            return false;
        }
        List<String> urlAcls = aclDao.selectAclsByUrl(url);
        if (CollectionUtils.isEmpty(urlAcls)) {
            return true;
        }
        List<String> userAcls = aclDao.selectAclsByUserId(url);
        return userAcls.containsAll(urlAcls);
    }


}
