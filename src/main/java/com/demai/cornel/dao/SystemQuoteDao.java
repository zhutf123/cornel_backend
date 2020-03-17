package com.demai.cornel.dao;

import com.demai.cornel.demeManager.vo.AdminGetQuoteListResp;
import com.demai.cornel.demeManager.vo.AdminGetSyQuLis;
import com.demai.cornel.model.SystemQuote;
import com.demai.cornel.vo.quota.GerQuoteListResp;
import com.demai.cornel.vo.quota.GetQuoteListReq;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-02    13:09
 */
public interface SystemQuoteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemQuote record);

    int insertSelective(SystemQuote record);

    SystemQuote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemQuote record);

    int updateByPrimaryKey(SystemQuote record);

    List<GerQuoteListResp> getNewSystemQuote(@Param("quoteId") String quoteId, @Param("pgSize") Integer pgSize);


    SystemQuote getSystemQuoteByCommodityId(@Param("commodityId")String commodityId);

    SystemQuote selectByCommodityId(String commodityId);

    List<AdminGetSyQuLis> adminGetSystemList();

    int updateCommoditySystemInvalid(@Param("commodityId")String commodityId);

    BigDecimal getNearestCommodityPrice(@Param("commodityId")String commodityId,@Param("time") String time);
}