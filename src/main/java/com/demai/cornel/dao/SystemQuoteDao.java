package com.demai.cornel.dao;

import com.demai.cornel.model.SystemQuote;

/**
* @Author binz.zhang
* @Date: 2020-01-02    13:09
*/
public interface SystemQuoteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemQuote record);

    int insertSelective(SystemQuote record);

    SystemQuote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemQuote record);

    int updateByPrimaryKey(SystemQuote record);
}