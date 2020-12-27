package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.CompanyInfo;

/**
 * @Author binz.zhang
 * @Date: 2020-02-20    00:00
 */
public interface CompanyInfoMapper {
    
    int insertSelective(CompanyInfo record);

    CompanyInfo selectByPrimaryKey(Integer id);
    CompanyInfo selectBycompanyId(String companyId);

    int updateByPrimaryKeySelective(CompanyInfo record);

    CompanyInfo selectByUserId(String userId);

}