package com.demai.cornel.dao;

import com.demai.cornel.model.QuoteInfo;

/**
* @Author binz.zhang
* @Date: 2019-12-31    13:39
*/
public interface QuoteInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(QuoteInfo record);

    int insertSelective(QuoteInfo record);

    QuoteInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuoteInfo record);

    int updateByPrimaryKey(QuoteInfo record);
}