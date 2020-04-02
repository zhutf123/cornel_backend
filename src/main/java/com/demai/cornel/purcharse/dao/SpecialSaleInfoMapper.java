package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.SpecialSaleInfo;
import com.demai.cornel.purcharse.vo.GetSystemOfferResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-04-02    11:42
 */
public interface SpecialSaleInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SpecialSaleInfo record);

    int insertSelective(SpecialSaleInfo record);

    SpecialSaleInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SpecialSaleInfo record);

    int updateByPrimaryKey(SpecialSaleInfo record);

    List<GetSystemOfferResp> getSystemOfferSheet(@Param("userId") String userId);

    SpecialSaleInfo selectSpecilaByCommodityIdAndUserId(@Param("commodityId") String commodityId,
            @Param("userId") String userId);

    int updateOfferStatusByCommodityIdAndUserId(@Param("commodityId") String commodityId,
            @Param("userId") String userId);
}