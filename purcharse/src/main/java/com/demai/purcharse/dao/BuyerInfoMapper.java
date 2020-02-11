package com.demai.purcharse.dao;

import com.demai.purcharse.model.BuyerInfo;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:27
*/
public interface BuyerInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BuyerInfo record);

    int insertSelective(BuyerInfo record);

    BuyerInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BuyerInfo record);

    int updateByPrimaryKey(BuyerInfo record);
}