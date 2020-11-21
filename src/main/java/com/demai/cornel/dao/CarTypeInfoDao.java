package com.demai.cornel.dao;

import com.demai.cornel.model.CarTypeInfo;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    15:21
 */
public interface CarTypeInfoDao {
    void insert(CarTypeInfo carTypeInfo);

    List<CarTypeInfo> selectAllCarType();
}
