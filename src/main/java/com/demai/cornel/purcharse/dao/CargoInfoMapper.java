package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.CargoInfo;

/**
 * @Author binz.zhang
 * @Date: 2020-02-14    19:58
 */
public interface CargoInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CargoInfo record);

    int insertSelective(CargoInfo record);

    CargoInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CargoInfo record);

    int updateByPrimaryKey(CargoInfo record);
}