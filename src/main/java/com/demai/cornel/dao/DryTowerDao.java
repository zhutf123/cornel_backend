package com.demai.cornel.dao;

import com.demai.cornel.model.DryTower;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @Author binz.zhang
* @Date: 2019-12-30    15:23
*/
public interface DryTowerDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DryTower record);

    int insertSelective(DryTower record);

    DryTower selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DryTower record);

    int updateByPrimaryKey(DryTower record);

    String getLocationByUserId(@Param("userID")String userId);


    List<DryTower> selectDryTowerByUserId(@Param("userID")String userId);
}