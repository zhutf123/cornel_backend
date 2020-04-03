package com.demai.cornel.purcharse.dao;

import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.purcharse.model.SaleOrder;
import com.demai.cornel.purcharse.vo.resp.GetSaleOrderListResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-18    21:51
 */
public interface SaleOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SaleOrder record);

    int insertSelective(SaleOrder record);

    SaleOrder selectByPrimaryKey(Integer id);

    SaleOrder selectBySaleId(String orderId);

    int updateByPrimaryKeySelective(SaleOrder record);

    int updateByPrimaryKey(SaleOrder record);

    SaleOrder selectSaleOrderByCargoId(@Param("cargoId") String cargoId);

    List<GetSaleOrderListResp> getSaleOrderList(@Param("orderId") String orderId, @Param("pgSize") Integer pgSize,
            @Param("status") Integer status);

    int updateSaleStatus(@Param("orderId") String orderId, @Param("status") Integer status);

    String getSaleIdByPurchaseId(@Param("purchaseId") String purchaseId);

    List<AdminGetSaleListResp> selectSaleView();

    AdminGetSaleList selectSaleList(String orderId);


    List<AdminGetSaleList> AdminGetSaleOrderList(@Param("viewStatus") Integer viewStatus,
            @Param("pgSize") Integer pgSize, @Param("offset") Integer offSet);



    int updateStackOutStackId(@Param("orderId")String orderId,@Param("outID")String outId);

    /**
     * 管理员获取待审核订单list
     * @param viewStatus
     * @param pgSize
     * @param offSet
     * @return
     */
    List<AdminUnRevSaleList> AdminGetUnRevSaleOrderList(@Param("viewStatus") Integer viewStatus,
            @Param("pgSize") Integer pgSize, @Param("offset") Integer offSet);

    /**
     * 管理员获取审核通过list
     * @param viewStatus
     * @param pgSize
     * @param offSet
     * @return
     */
    List<AdminAppSaleList> AdminGetAppSaleOrderList(@Param("viewStatus") Integer viewStatus,
            @Param("pgSize") Integer pgSize, @Param("offset") Integer offSet);

}