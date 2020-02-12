package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.PurchaseInfo;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:29
*/
public interface PurchaseInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PurchaseInfo record);

    int insertSelective(PurchaseInfo record);

    PurchaseInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PurchaseInfo record);

    int updateByPrimaryKey(PurchaseInfo record);
}