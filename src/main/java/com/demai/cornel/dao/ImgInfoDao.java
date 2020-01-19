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

}