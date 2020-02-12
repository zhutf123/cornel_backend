package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.CompanyInfo;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:16
*/
public interface CompanyInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CompanyInfo record);

    int insertSelective(CompanyInfo record);

    CompanyInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CompanyInfo record);

    int updateByPrimaryKey(CompanyInfo record);
}