package com.demai.purcharse.dao;

import com.demai.purcharse.model.CargoId;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:27
*/
public interface CargoIdMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CargoId record);

    int insertSelective(CargoId record);

    CargoId selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CargoId record);

    int updateByPrimaryKey(CargoId record);
}