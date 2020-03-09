package com.demai.purcharse.dao;

import com.demai.purcharse.model.LocationInfo;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:24
*/
public interface LocationInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LocationInfo record);

    int insertSelective(LocationInfo record);

    LocationInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LocationInfo record);

    int updateByPrimaryKey(LocationInfo record);
}