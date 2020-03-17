package com.demai.cornel.dao;

import com.demai.cornel.model.ImgInfo;
import com.demai.cornel.model.QuoteInfo;
import com.demai.cornel.vo.quota.GetOfferInfoResp;
import com.demai.cornel.vo.quota.GetOfferListResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-06    11:58
 */
public interface ImgInfoDao {

    int insert(ImgInfo imgInfo);

    //    int updateCarImgURL(@Param("desc")String desc,@Param("lorryId")String lorryId,@Param("status")Integer status);
    //
    //    int updateUserImgURL(@Param("desc")String desc,@Param("userId")String userId,@Param("status")Integer status);

    List<ImgInfo> getCarImgByLorryId(@Param("lorryId") String lorryId);

    List<ImgInfo> getUserImgByUserId(@Param("userId") String userId);

    List<ImgInfo> getUserImgByQuoteId(@Param("quoteId") String quoteId);


    int updateImgUrl(@Param("bindId")String bindId,@Param("desc")String desc,@Param("url") String url );
}