package com.demai.cornel.purcharse.dao;

import com.demai.cornel.demeManager.vo.AdminGetFreightViewResp;
import com.demai.cornel.purcharse.model.FreightInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    13:09
 */
public interface FreightInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FreightInfo record);

    int insertSelective(FreightInfo record);

    FreightInfo selectByPrimaryKey(Integer id);

    FreightInfo selectByFreightId(String FreightId);

    int updateByPrimaryKeySelective(FreightInfo record);

    int updateByPrimaryKey(FreightInfo record);

    FreightInfo selectMinPriceRoute(@Param("from") String from, @Param("to") String to);

    List<FreightInfo> selectFreights(@Param("from") String from, @Param("to") String to);

    List<AdminGetFreightViewResp> adminnGetFreightView(@Param("offset") Integer offset,
            @Param("pgSize") Integer pgSzie);

}