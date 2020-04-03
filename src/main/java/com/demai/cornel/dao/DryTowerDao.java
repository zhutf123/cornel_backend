package com.demai.cornel.dao;

import com.demai.cornel.demeManager.vo.AdminGetFreightViewResp;
import com.demai.cornel.model.DryTower;
import com.demai.cornel.model.LoanInfo;
import com.demai.cornel.purcharse.model.LocationInfo;
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

    /**
     * 获取指定人下面的烘干塔信息 排序方式默认烘干塔在前 后面按时间倒序排列
     * @param userId
     * @return
     */
    List<DryTower> selectDryTowerByUserId(@Param("userID")String userId);


    DryTower selectByTowerId(@Param("towerId")String towerId);

    int updateTowerNonDefaultFlag(@Param("userID")String userId);

    List<DryTower> selectAllTower(@Param("towerId")String towerId,@Param("pgSize")Integer pgSize);


    List<AdminGetFreightViewResp> adminGetDryTower(@Param("pgSize")Integer pgSize,@Param("offset")Integer offset);

    AdminGetFreightViewResp adminGetDryTowerFreiViewByTowerId(@Param("towerId")String towerID);


    List<LocationInfo> getLocation();
}