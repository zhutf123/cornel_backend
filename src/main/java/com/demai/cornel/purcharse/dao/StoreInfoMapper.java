package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.StoreInfo;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:08
*/
public interface StoreInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StoreInfo record);

    int insertSelective(StoreInfo record);

    StoreInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StoreInfo record);

    int updateByPrimaryKey(StoreInfo record);
}