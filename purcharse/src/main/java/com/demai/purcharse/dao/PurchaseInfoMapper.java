package com.demai.purcharse.dao;

import com.demai.purcharse.model.PurchaseInfo;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:27
*/
public interface PurchaseInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PurchaseInfo record);

    int insertSelective(PurchaseInfo record);

    PurchaseInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PurchaseInfo record);

    int updateByPrimaryKey(PurchaseInfo record);
}