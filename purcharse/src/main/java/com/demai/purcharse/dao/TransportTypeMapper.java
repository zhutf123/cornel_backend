package com.demai.purcharse.dao;

import com.demai.purcharse.model.TransportType;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:28
*/
public interface TransportTypeMapper {
    int insert(TransportType record);

    int insertSelective(TransportType record);
}