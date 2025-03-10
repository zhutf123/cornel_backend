package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.PurchaseInfo;
import com.demai.cornel.purcharse.vo.resp.GetPurchaseDetailResp;
import com.demai.cornel.purcharse.vo.resp.GetPurchaseListResp;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-18    21:31
 */
public interface PurchaseInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PurchaseInfo record);

    int insertSelective(PurchaseInfo record);

    PurchaseInfo selectByPrimaryKey(Integer id);

    PurchaseInfo selectByPurchaseId(String purchaseId);

    int updateByPrimaryKeySelective(PurchaseInfo record);


    int updateStatus(@Param("purchaseId")String purchaseId,@Param("status")Integer status);
    int updateByPrimaryKey(PurchaseInfo record);


   List<PurchaseInfo> getPurcharseList(@Param("buyserId")String buyerId,
           @Param("purchaseId")String purchaseId,@Param("pgSize")Integer pgSize);

//   GetPurchaseDetailResp getPurchaseDetail(@Param("purchaseId")String purchaseId);

   int getPurchaseNum(@Param("userId")String userId);
}