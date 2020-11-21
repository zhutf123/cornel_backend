package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.TransportType;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    16:59
 */
public interface TransportTypeMapper {
    int insert(TransportType record);

    int insertSelective(TransportType record);
}