package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.CargoId;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:34
*/
public interface CargoIdMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CargoId record);

    int insertSelective(CargoId record);

    CargoId selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CargoId record);

    int updateByPrimaryKey(CargoId record);
}