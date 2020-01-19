/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.model.CarCornInfo;
import com.demai.cornel.model.DriverCornInfo;
import com.demai.cornel.service.DeliveryTaskService;
import com.demai.cornel.service.DriverCornService;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.order.GetOrderInfoReq;
import com.demai.cornel.vo.order.OperationOrderReq;
import com.demai.cornel.vo.order.OperationOrderResp;
import com.demai.cornel.vo.supplier.SupplierTaskListResp;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.demai.cornel.vo.task.GetOrderListResp;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
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
 * Create By zhutf  19-11-10  上午9:33
 */
@Controller
@RequestMapping("/delivery")
@Slf4j
public class DeliveryCornController {
    @Resource
    private DeliveryTaskService deliveryTaskService;
    @Resource
    private DriverCornService driverCornService;
    /**
     * 接货人列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/list.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deliveryList(@RequestBody GetOrderListReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            if (param.getOrderType() == null) {
                log.error("delivery task list query task list is null:{}", curUser);
                return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
            }
            if (log.isDebugEnabled()) {
                log.debug("delivery task list user:{} status:{}", curUser, param);
            }
            Collection<SupplierTaskListResp> result = deliveryTaskService.getTaskOrderListByStatus(curUser, param);
            if (log.isDebugEnabled()) {
                log.debug("delivery task list user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);

        } catch (Exception e) {
            log.error("delivery task list exception:{}", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }

    /**
     * 接货人列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/listV2.json", method = RequestMethod.POST) @ResponseBody public JsonResult deliveryListV2(
            @RequestBody GetOrderListReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            if (param.getOrderType() == null) {
                log.error("delivery task list query task list is null:{}", curUser);
                return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
            }
            if (log.isDebugEnabled()) {
                log.debug("delivery task list user:{} status:{}", curUser, param);
            }
            List<GetOrderListResp> result = deliveryTaskService.getTaskOrderListByStatusV2(curUser, param);
            if (log.isDebugEnabled()) {
                log.debug("delivery task list user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);

        } catch (Exception e) {
            log.error("delivery task list exception:{}", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }

    /**
     * 接货人查看订单详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/order-info.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult orderInfo(@RequestBody GetOrderInfoReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            if (param == null || (StringUtil.isEmpty(param.getOrderId()) && StringUtil.isEmpty(param.getVerifyCode()))
                    || ("null".equalsIgnoreCase(param.getOrderId())) && "null"
                    .equalsIgnoreCase(param.getVerifyCode())) {
                log.error("delivery task order info user:{}", curUser);
                return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
            }
            GetOrderListResp result = deliveryTaskService.getTaskOrderInfoByOrderIdOrVerifyCode(curUser, param);
            if (log.isDebugEnabled()) {
                log.debug("delivery task order info  user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);
        } catch (Exception e) {
            log.error("delivery task order info exception ！", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }


    /**
     * 接货人确认 开始卸货
     * @param param
     * @return
     */
    @RequestMapping(value = "/confirm.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deliveryConfirm(@RequestBody GetOrderInfoReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            OperationOrderResp result = deliveryTaskService.deliveryStart(curUser, param.getOrderId());
            if (log.isDebugEnabled()) {
                log.debug("delivery task start user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);
        }catch (Exception e){
            log.error("delivery task start exception ！{}", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }

    /**
     * 卸货完成
     * @param param
     * @return
             */
    @RequestMapping(value = "/over.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deliveryOver(@RequestBody OperationOrderReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            OperationOrderResp result = deliveryTaskService.deliveryOver(curUser, param);
            if (log.isDebugEnabled()) {
                log.debug("delivery task over user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);
        }catch (Exception e){
            log.error("delivery task over exception！{}", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }

    /**
     * 司机侧 "我的" 获取司机关键信息
     * @return
     */
    @RequestMapping(value = "/user-info.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deliveryInfo() {
        return JsonResult.success(driverCornService.getUserCornInfo());
    }

    /**
     * 司机侧 "我的" 获取司机 车辆信息list
     * @return
     */
    @RequestMapping(value = "/car-list.json", method = RequestMethod.POST)
    @ResponseBody
    //TODO
    public JsonResult deliveryList() {
        return JsonResult.success(driverCornService.getCarCornInfoList());
    }

    /**
     * 司机测 获取车辆的关键信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/car-info.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deliveryCarInfo(@RequestBody String param) {
        if (Strings.isNullOrEmpty(param)) {
            return JsonResult.error("param illegal");
        }
        JSONObject receivedParam = JSON.parseObject(param);
        String lorryId = (String) receivedParam.get("lorryId");
        return JsonResult.success(driverCornService.getCarCornInfo(lorryId));
    }
    /**
     * 司机测 编辑车辆的关键信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/edit-carinfo.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deliveryUpdateCarInfo(@RequestBody CarCornInfo carCornInfo) {
        Preconditions.checkNotNull(carCornInfo);
        return JsonResult.success(driverCornService.updateCarCornInfo(carCornInfo));
    }
    /**
     * 司机测 编辑个人的关键信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/edit-userinfo.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deliveryUpdateDriverInfo(@RequestBody DriverCornInfo driverCornInfo) {
        Preconditions.checkNotNull(driverCornInfo);
        return JsonResult.success(driverCornService.updateUserCornInfo(driverCornInfo));
    }

}
