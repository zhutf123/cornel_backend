package com.demai.cornel.purcharse.dao;

import com.demai.cornel.demeManager.model.FreightWithToLocation;
import com.demai.cornel.demeManager.vo.AdminGetFreightViewResp;
import com.demai.cornel.purcharse.model.FreightInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    13:09
 */
public interface FreightInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FreightInfo record);

    int insertSelective(FreightInfo record);

    int updateStatus(@Param("freightId") String freightId);

    FreightInfo selectByPrimaryKey(Integer id);

    FreightInfo selectByFreightId(String FreightId);

    int updateByPrimaryKeySelective(FreightInfo record);

    int updateByPrimaryKey(FreightInfo record);

    FreightInfo selectMinPriceRoute(@Param("from") String from, @Param("to") String to);

//    List<FreightInfo> selectFreights(@Param("from") String from, @Param("to") String to);

    List<FreightInfo> selectFreightsByLocationArea(@Param("from") String fromLocationArea, @Param("to") String toLocationArea);



    AdminGetFreightViewResp adminnGetOptFreightView(@Param("fromLocationId") String fromLocationId);

    List<FreightWithToLocation> adminGetFreightWithToLocaByFromLoc(@Param("fromLocation") String fromLocationId);



    Set<String> selectFreightIdByFromLocation(@Param("fromLocation")String fromLocation);

  int updateStatusOtherFreightId(@Param("fromLocationId")List<String> freightId,@Param("fromLocation")String fromLocation,@Param("toLocation")String toLocation);

  int updateFreightStatusByFreightIds(@Param("freightIds")Set<String> freightId);


}