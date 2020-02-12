package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.SaleOrder;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:32
*/
public interface SaleOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SaleOrder record);

    int insertSelective(SaleOrder record);

    SaleOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SaleOrder record);

    int updateByPrimaryKey(SaleOrder record);
}