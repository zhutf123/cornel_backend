package com.demai.cornel.dao;

import com.demai.cornel.demeManager.vo.AdminGetQueFinResp;
import com.demai.cornel.demeManager.vo.AdminGetQuoteList;
import com.demai.cornel.demeManager.vo.AdminGetQuoteListResp;
import com.demai.cornel.demeManager.vo.AdminGetQuteDetail;
import com.demai.cornel.model.QuoteInfo;
import com.demai.cornel.vo.quota.GetOfferInfoResp;
import com.demai.cornel.vo.quota.GetOfferListResp;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-06    11:58
 */
public interface QuoteInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(QuoteInfo record);

    int insertSelective(QuoteInfo record);

    QuoteInfo selectByPrimaryKey(String quoteId);

    int updateByPrimaryKeySelective(QuoteInfo record);

    int updateByPrimaryKey(QuoteInfo record);

    List<GetOfferListResp> getOwnerQuoteList(@Param("userId") String userId, @Param("quoteId") String quoteId,
            @Param("pgSize") Integer pgSize);

    List<GetOfferListResp> getSystemOwnerQuoteList(@Param("userId") String userId, @Param("quoteId") String quoteId,
            @Param("pgSize") Integer pgSize);

    List<AdminGetQuoteListResp> adminGetQuoteList(@Param("limit") Integer quoteId, @Param("pgSize") Integer pgSize,@Param("towerId")String towerId);

    List<AdminGetQuoteListResp> opPdminGetQuoteList(@Param("limit") Integer quoteId, @Param("pgSize") Integer pgSize,@Param("towerId")String towerId);


    GetOfferInfoResp getQuoteInfoById(@Param("quoteId") String quoteId);

    AdminGetQuteDetail adminGetQuoteDetail(@Param("quoteId") String quoteId);

    int updateStatusByQuoteIdAndUserId(@Param("quoteId") String quoteId, @Param("userId") String userId,
            @Param("status") int status);

    List<GetOfferListResp> getSystemOwnerQuoteListV2(@Param("userId") String userId, @Param("time") Timestamp timestamp,
            @Param("pgSize") Integer pgSize);

    List<AdminGetQuoteList.orderInfo> selectQuoteViewByTowerId(@Param("towerId") String towerId);


    List<AdminGetQuoteList.orderInfo> optselectQuoteViewByTowerId(@Param("towerId") String towerId);


    AdminGetQueFinResp adminGetFinInfo();
}