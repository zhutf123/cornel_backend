package com.demai.cornel.purcharse.dao;

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

    int updateByPrimaryKeySelective(SaleOrder record);

    int updateByPrimaryKey(SaleOrder record);

    SaleOrder selectSaleOrderByCargoId(@Param("cargoId") String cargoId);

    List<GetSaleOrderListResp> getSaleOrderList(@Param("orderId") String orderId, @Param("pgSize") Integer pgSize,
            @Param("status") Integer status);
}