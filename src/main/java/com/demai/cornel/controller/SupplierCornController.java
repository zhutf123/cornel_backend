/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.service.DistOrderService;
import com.demai.cornel.service.SupplierTaskService;
import com.demai.cornel.service.impl.TaskServiceImp;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.order.GetOrderInfoReq;
import com.demai.cornel.vo.order.OperationOrderReq;
import com.demai.cornel.vo.order.OperationOrderResp;
import com.demai.cornel.vo.supplier.SupplierTaskListResp;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.demai.cornel.vo.task.GetOrderListResp;
import groovy.transform.Undefined;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.JsonResult;

import lombok.extern.slf4j.Slf4j;

/**
 * Create By zhutf 19-11-10 上午9:33
 */
@Controller
@RequestMapping("/supply")
@Slf4j
public class SupplierCornController {
    @Resource
    private SupplierTaskService supplierTaskService;

    /**
     * 烘干塔列表
     * 
     * @param param
     * @return
     */
    @RequestMapping(value = "/shipment-list.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipmentList(@RequestBody GetOrderListReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            if (param.getOrderType() == null) {
                log.error("supplier task order list param error:{}", curUser);
                return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
            }
            
            Collection<SupplierTaskListResp> result = supplierTaskService.getTaskOrderListByStatus(curUser, param);
            if (log.isDebugEnabled()) {
                log.debug("supplier task order list user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);

        } catch (Exception e) {
            log.error("supplier task order list exception:{}", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }


    /**
     * 烘干塔列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/shipment-listV2.json", method = RequestMethod.POST) @ResponseBody public JsonResult shipmentListV2(
            @RequestBody GetOrderListReq param) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("supplier task order shipment-listV2 param:{}", JsonUtil.toJson(param));
            }
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            if (param.getOrderType() == null) {
                log.error("supplier task order list param error:{}", curUser);
                return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
            }

            List<GetOrderListResp> result = supplierTaskService.getTaskOrderListByStatusV2(curUser, param);
            if (log.isDebugEnabled()) {
                log.debug("supplier task order list user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);
        } catch (Exception e) {
            log.error("supplier task order list exception:{}", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }

    /**
     * 烘干塔查看订单详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/order-info.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult orderInfo(@RequestBody GetOrderInfoReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            if (param == null) {
                log.error("supplier task order info param error :{}", curUser);
                return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
            }
            GetOrderListResp result = supplierTaskService.getTaskOrderInfoByOrderIdOrVerifyCode(curUser, param);
            if (log.isDebugEnabled()) {
                log.debug("supplier task order info user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            
            return JsonResult.success(result);
        } catch (Exception e) {
            log.error("supplier task order info exception！", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }

    /**
     * 烘干塔开始装货
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/shipment.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipmentStart(@RequestBody String orderId) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            OperationOrderResp result = supplierTaskService.shipmentStart(curUser, orderId);
            if (log.isDebugEnabled()) {
                log.debug("supplier task order shipment start user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);
        }catch (Exception e){
            log.error("supplier task order shipment start exception！{}", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }

    /**
     * 烘干塔装货完成
     * 
     * @param param
     * @return
     */
    @RequestMapping(value = "/shipment-over.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipmentOver(@RequestBody OperationOrderReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            OperationOrderResp result = supplierTaskService.shipmentOver(curUser, param);
            if (log.isDebugEnabled()) {
                log.debug("supplier task order shipment over user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);
        }catch (Exception e){
            log.error("supplier task order shipment over exception！{}", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }
}
