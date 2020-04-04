package com.demai.cornel.purcharse.dao;

import com.demai.cornel.demeManager.vo.AdminLocationMode;
import com.demai.cornel.purcharse.model.LocationInfo;import org.apache.ibatis.annotations.Param;import java.util.List;import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-02-14    10:58
 */
public interface LocationInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LocationInfo record);

    int insertSelective(LocationInfo record);

    LocationInfo selectByPrimaryKey(Integer id);
    LocationInfo selectByLocationId(@Param("locationId") String locationId);

    int updateByPrimaryKeySelective(LocationInfo record);

    int updateByPrimaryKey(LocationInfo record);

    List<String> getLocationByLocationId(@Param("locationId") Set<String> locations);
    List<LocationInfo> getLocationInfoByLocationId(@Param("locationId") Set<String> locations);

    LocationInfo selectLocationIdByInfo(@Param("locationArea")String locationArea,@Param("locationDetail")String locationDetail,@Param("location")String location);

   List<AdminLocationMode> getSystemLocation(@Param("offset")Integer offset,@Param("pgSize")Integer pgSize);
    AdminLocationMode selectLocationModelByInfo(@Param("locationArea")String locationArea,@Param("locationDetail")String locationDetail,@Param("location")String location);

    int updateLocationInfoByLocationId(@Param("locationArea")String locationArea,@Param("locationDetail")String locationDetail,@Param("location")String location,@Param("locationId") String locationId);

    int insertSelectiveAdminLocationMode(AdminLocationMode locationMode);
}