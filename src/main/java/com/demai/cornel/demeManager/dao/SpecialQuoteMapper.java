package com.demai.cornel.demeManager.dao;

import com.demai.cornel.demeManager.model.SpecialQuote;
import org.apache.ibatis.annotations.Param;

/**
* @Author binz.zhang
* @Date: 2020-03-08    19:37
*/
public interface SpecialQuoteMapper {
    int insert(SpecialQuote record);

    int insertSelective(SpecialQuote record);


    int updateCommodityIdquoteStatus(@Param("commodityId")String commodityId,@Param("userId")String userId);
}