package com.demai.cornel.demeManager.dao;

import com.demai.cornel.demeManager.model.SpecialQuote;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    19:37
 */
public interface SpecialQuoteMapper {
    int insert(SpecialQuote record);

    int insertSelective(SpecialQuote record);

    int updateCommodityIdquoteStatus(@Param("commodityId") String commodityId, @Param("userId") String userId);

    List<SpecialQuote> selectSpecialQuoteByTargetTowerId(@Param("towerId") String towerId);

    BigDecimal getNearestCommodityPrice(@Param("userId")String userId,@Param("commodityId")String commodityId,@Param("time") Date time);

    Timestamp getNearestCommodityPriceTime(@Param("userId")String userId,@Param("commodityId")String commodityId);


    SpecialQuote selectSpecialQuoteByCommodityId(@Param("towerId")String towerId,@Param("commodityId")String commodityId);
}