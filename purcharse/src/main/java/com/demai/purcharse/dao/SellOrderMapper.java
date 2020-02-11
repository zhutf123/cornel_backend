package com.demai.purcharse.dao;

import com.demai.purcharse.model.SellOrder;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:27
*/
public interface SellOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SellOrder record);

    int insertSelective(SellOrder record);

    SellOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SellOrder record);

    int updateByPrimaryKey(SellOrder record);
}