package com.demai.purcharse.dao;

import com.demai.purcharse.model.CompanyInfo;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:27
*/
public interface CompanyInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CompanyInfo record);

    int insertSelective(CompanyInfo record);

    CompanyInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CompanyInfo record);

    int updateByPrimaryKey(CompanyInfo record);
}