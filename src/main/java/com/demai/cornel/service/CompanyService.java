/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import com.demai.cornel.purcharse.dao.CompanyInfoMapper;
import com.demai.cornel.purcharse.model.CompanyInfo;
import com.demai.cornel.vo.user.CompanyParam;
import groovy.util.logging.Commons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Create By tfzhu  2020/12/27  11:55 AM
 *
 * @author tfzhu
 */
@Service @Slf4j
public class CompanyService {

    @Resource CompanyInfoMapper companyInfoMapper;

    public String addCompanyInfo(CompanyParam param){
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setCompanyName(param.getCompanyName());
        companyInfo.setCompanyId(UUID.randomUUID().toString());
        companyInfo.setUserId(param.getUserId());
        companyInfo.setLicenseUrl(param.getLicenseUrl());
        companyInfoMapper.insertSelective(companyInfo);
        return companyInfo.getCompanyId();
        
    }

}
