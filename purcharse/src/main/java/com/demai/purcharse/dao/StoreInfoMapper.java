package com.demai.purcharse.dao;

import com.demai.purcharse.model.StoreInfo;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:27
*/
public interface StoreInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StoreInfo record);

    int insertSelective(StoreInfo record);

    StoreInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StoreInfo record);

    int updateByPrimaryKey(StoreInfo record);
}