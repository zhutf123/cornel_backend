package com.demai.cornel.purcharse.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.annotation.AccessControl;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.purcharse.model.PurchaseInfo;
import com.demai.cornel.purcharse.service.DistSaleOrderService;
import com.demai.cornel.purcharse.service.PurchaseCornService;
import com.demai.cornel.purcharse.vo.req.*;
import com.demai.cornel.service.UserLoginService;
import com.demai.cornel.service.UserService;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.demai.cornel.vo.user.UserLoginSendMsgParam;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.naming.ldap.PagedResultsControl;
import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    14:16
 */

@Controller @RequestMapping("/purchase") @Slf4j public class PurchaseCornController {
    @Resource private PurchaseCornService purchaseCornService;
    @Resource private UserService userService;
    @Resource private DistSaleOrderService distSaleOrderService;

    /**
     * 获取系统报价
     *
     * @return
     */
    @RequestMapping(value = "/get-system-offer.json", method = RequestMethod.POST) @ResponseBody public JsonResult systemOffer(
            @RequestBody GetSystemOfferReq getSystemOfferReq) {
        return JsonResult.success(purchaseCornService.getSystemOfferRespList(getSystemOfferReq));
    }

    /**
     * 获取系统报价
     *
     * @return
     */
    @RequestMapping(value = "/click-system-offer.json", method = RequestMethod.POST) @ResponseBody public JsonResult clickSystemOffe(
            @RequestBody String param) {
        Preconditions.checkNotNull(param);
        JSONObject receivedParam = JSON.parseObject(param);
        String orderId = (String) receivedParam.get("offerId");
        return JsonResult.success(purchaseCornService.clickSystemQuoteResp(orderId));
    }

    /**
     * 点击我要报价 系统补充信息
     *
     * @return
     */
    @RequestMapping(value = "/click-my-offer.json", method = RequestMethod.POST) @ResponseBody public JsonResult clickSystemOffe() {
        return JsonResult.success(purchaseCornService.clickMyOfferResp());
    }

    /**
     * 点击我要报价 系统补充信息
     *
     * @return
     */
    @RequestMapping(value = "/submit-my-offer.json", method = RequestMethod.POST) @ResponseBody public JsonResult submitMyOffer(
            @RequestBody SubmitMyOfferReq param) {
        return JsonResult.success(purchaseCornService.submitMyOffer(param));
    }

    /**
     * 买家下单系统商品操作
     *
     * @return
     */
    @RequestMapping(value = "/submit-system-offer.json", method = RequestMethod.POST) @ResponseBody public JsonResult systemOffer(
            @RequestBody SystemOfferReq param) {
        return JsonResult.success(purchaseCornService.submitSystemQuoteResp(param));
    }

    /**
     * 买家获取下单列表
     *
     * @return
     */
    @RequestMapping(value = "/get-order-list.json", method = RequestMethod.POST) @ResponseBody public JsonResult systemOffer(
            @RequestBody GetSaleOrderListReq getSaleOrderListReq) {
        log.info("user {} get order list param is {}", CookieAuthUtils.getCurrentUser(),JacksonUtils.obj2String(getSaleOrderListReq));
        return JsonResult.success(purchaseCornService.getSaleOrderListRespList(getSaleOrderListReq));
    }

    /**
     * 买家获取指定订单下的货运列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/get-car-list.json", method = RequestMethod.POST) @ResponseBody public JsonResult systemOffer(
            @RequestBody String param ) {
        JSONObject jsonObject = JSON.parseObject(param);
        String saleId = jsonObject.getString("saleId");

        return JsonResult.success(purchaseCornService.getSaleList(saleId));
    }

    /**
     * 买家获取指定货运的 货运详情
     * @param param
     * @return
     */
    @RequestMapping(value = "/get-car-detail.json", method = RequestMethod.POST) @ResponseBody public JsonResult getdeliverDetail(
            @RequestBody String param ) {
        JSONObject jsonObject = JSON.parseObject(param);
        String deliverOrderId = jsonObject.getString("deliverOrderId");
        return JsonResult.success(purchaseCornService.getDeliverOrderDetail(deliverOrderId));
    }

    @RequestMapping(value = "/confirm-receive.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult confirmReceive(@RequestBody String param ) {
        JSONObject jsonObject = JSON.parseObject(param);
        String deliverOrderId = jsonObject.getString("deliverOrderId");
        String saleId = jsonObject.getString("saleId");
        BigDecimal receiveWeight = jsonObject.getBigDecimal("receiveWeight");
        return JsonResult.success(purchaseCornService.confirmDeliver(saleId,deliverOrderId,receiveWeight));
    }

    /**
     * 买家获取订单详情
     * @param param
     * @return
     */
    @RequestMapping(value = "/get-order-detail.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult getSaleDetail(@RequestBody String param) {
        JSONObject jsonObject = JSON.parseObject(param);
        String saleId = jsonObject.getString("saleId");
        log.info("purchase get get-order-detail sale id is {}",saleId);
        return JsonResult.success(purchaseCornService.getSaleOrderDetail(saleId));
    }


    /**
     * 买家获取我的求购订单list
     *
     * @return
     */
    @RequestMapping(value = "/get-purchase-list.json", method = RequestMethod.POST) @ResponseBody public JsonResult systemOffer(
            @RequestBody GetPurchaseListReq getPurchaseListReq) {
        return JsonResult.success(purchaseCornService.getPurchaseListRespList(getPurchaseListReq));
    }

    /**
     * 买家获取我的求购总数目
     *
     * @return
     */
    @RequestMapping(value = "/get-purchase-num.json", method = RequestMethod.POST) @ResponseBody public JsonResult getPurchaseNum() {
        return JsonResult.success(purchaseCornService.getPurchaseNum());
    }

    /**
     * 买家获取我的求购的详情
     *
     * @return
     */
    @RequestMapping(value = "/get-purchase-detail.json", method = RequestMethod.POST) @ResponseBody public JsonResult getPurchaseDetai(
            @RequestBody String param) {
        Preconditions.checkNotNull(param);
        JSONObject receivedParam = JSON.parseObject(param);
        String orderId = (String) receivedParam.get("purchaseId");
        return JsonResult.success(purchaseCornService.getPurchaseDetailResp(orderId));
    }

    /**
     * 买家编辑自己的求购订单
     *
     * @return
     */
    @RequestMapping(value = "/edit-purchase.json", method = RequestMethod.POST) @ResponseBody public JsonResult editPurchase(
            @RequestBody SubmitMyOfferReq param) {
        return JsonResult.success(purchaseCornService.editPurchase(param));
    }

    /**
     * 买家下单系统商品操作
     *
     * @return
     */
    @RequestMapping(value = "/update-purchase-status.json", method = RequestMethod.POST) @ResponseBody public JsonResult updatePurchasestatus(
            @RequestBody String param) {
        Preconditions.checkNotNull(param);
        JSONObject receivedParam = JSON.parseObject(param);
        String orderId = (String) receivedParam.get("purchaseId");
        Integer status = (Integer) receivedParam.get("status");

        return JsonResult.success(purchaseCornService.updatePurchase(orderId, status));
    }

    @RequestMapping(value = "/get-purchase-bargain.json", method = RequestMethod.POST) @ResponseBody public JsonResult getPurchaseBargain(
            @RequestBody String param) {
        Preconditions.checkNotNull(param);
        JSONObject receivedParam = JSON.parseObject(param);
        String orderId = (String) receivedParam.get("purchaseId");
        return JsonResult.success(purchaseCornService.getPurcahseBargain(orderId));
    }

    @RequestMapping(value = "/edit-purchase-price.json", method = RequestMethod.POST) @ResponseBody public JsonResult updatePurchasePrice(
            @RequestBody UpdatePurcahsePriceReq param) {
        Preconditions.checkNotNull(param);
        return JsonResult.success(purchaseCornService.updatePurchasePrice(param));
    }



    //    /**
    //     * 买家派单逻辑
    //     * @return
    //     */
    @RequestMapping(value = "/dist-order.json", method = RequestMethod.POST) @ResponseBody public JsonResult getOrderList(
            @RequestBody String param) {
        JSONObject jsonObject1 = JSONObject.parseObject(param);
        String cargoId = jsonObject1.getString("saleId");
        return JsonResult.success(distSaleOrderService.cargoConvertTask(cargoId));
    }

}
