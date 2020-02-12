package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.LocationInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    15:22
 */
public interface LocationInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LocationInfo record);

    int insertSelective(LocationInfo record);

    LocationInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LocationInfo record);

    int updateByPrimaryKey(LocationInfo record);

    List<String> getLocationByLocationId(@Param("locationId") Set<String> locations);
}