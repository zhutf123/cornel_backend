package com.demai.cornel.demeManager.web.purchase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.demeManager.service.AdminPurchaseCornService;
import com.demai.cornel.demeManager.service.AdminSaleOrderService;
import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.vo.JsonResult;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    12:06
 */
@Controller @RequestMapping("/admin") @Slf4j public class PurchaseController {

    @Resource private AdminPurchaseCornService adminPurchaseCornService;
    @Resource private AdminSaleOrderService adminSaleOrderService;

    /**
     * 管理员获取买家系统的报价list
     *
     * @return
     */
    @RequestMapping(value = "/get-sys-offer.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult adminGetSysOfferList() {
        return JsonResult.success(adminPurchaseCornService.adminGetSysOffList());
    }

    /**
     * 管理员编辑买家端的价格 可以修改系统的价格 也可以修改针对特定买家的价格
     *
     * @return
     */
    @RequestMapping(value = "/edit-sys-offer.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult editSysOfferQuote(
            @RequestBody AdminEditSysOfferReq adminEditSysOfferReq) {
        Preconditions.checkNotNull(adminEditSysOfferReq);
        return JsonResult.success(adminPurchaseCornService.adminEditSysOfferQuote(adminEditSysOfferReq));
    }

    /**
     * 管理员获取买家list
     *
     * @param adminGetBuyerReq
     * @return
     */
    @RequestMapping(value = "/get-buyer-list.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult getBuyerList(
            @RequestBody AdminGetBuyerReq adminGetBuyerReq) {
        Preconditions.checkNotNull(adminGetBuyerReq);
        return JsonResult.success(adminPurchaseCornService.adminGetBuyerList(adminGetBuyerReq));
    }

    /**
     * 管理员获取下单列表view
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/get-order-view.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult getOrderView() {
        return JsonResult.success(adminSaleOrderService.getSaleView());
    }

    /**
     * 管理员获取下单列表view
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/get-order-list.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult adminGetOrderList(
            @RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        Integer status = (Integer) receivedParam.get("status");
        Integer pgSize = (Integer) receivedParam.get("pgSize");
        Integer offSet = (Integer) receivedParam.get("offSet");
        return JsonResult.success(adminSaleOrderService.adminGetSaleByStatus(status, offSet, pgSize));
    }

    @RequestMapping(value = "/review-sale.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult reviewSaleOrder(
            @RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        Integer status = (Integer) receivedParam.get("status");
        String offSet = (String) receivedParam.get("saleId");
        return null;
    }
}
