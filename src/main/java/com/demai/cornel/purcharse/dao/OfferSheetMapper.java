package com.demai.cornel.purcharse.dao;

import com.demai.cornel.demeManager.vo.AdminGetSysOffResp;
import com.demai.cornel.purcharse.model.OfferSheet;
import com.demai.cornel.purcharse.vo.GetSystemOfferResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    17:06
 */
public interface OfferSheetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OfferSheet record);

    int insertSelective(OfferSheet record);

    OfferSheet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OfferSheet record);

    int updateByPrimaryKey(OfferSheet record);

    List<GetSystemOfferResp> getSystemOfferSheet(@Param("offerId") String offerId, @Param("pgSize") Integer pgSize);

    List<AdminGetSysOffResp> adminGetSysOfferSheet();

    OfferSheet selectByOfferId(@Param("offerId") String offerId);

    OfferSheet selectByCommodityId(@Param("commodityId") String commodityId);

    int updateOfferStatusByCommodityId(@Param("commodityId") String commodityId);

    int updateOfferStatusByCommodityIdAndUserId(@Param("commodityId") String commodityId,
            @Param("userId") String userId);

    OfferSheet selectSpecilaByCommodityIdAndUserId(@Param("commodityId") String commodityId,
            @Param("userId") String userId);

    List<GetSystemOfferResp> getSystemOfferSheetAndUserId(@Param("offerId") String offerId,
            @Param("pgSize") Integer pgSize, @Param("userId") String userId);

}