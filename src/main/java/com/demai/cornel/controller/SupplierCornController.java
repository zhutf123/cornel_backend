/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.annotation.AccessControl;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.DryTower;
import com.demai.cornel.service.SupplierTaskService;
import com.demai.cornel.service.SupplyUserService;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.order.GetOrderInfoReq;
import com.demai.cornel.vo.order.OperationOrderReq;
import com.demai.cornel.vo.order.OperationOrderResp;
import com.demai.cornel.vo.supplier.AddDryTowerReq;
import com.demai.cornel.vo.supplier.SupplierRegisterReq;
import com.demai.cornel.vo.supplier.SupplierTaskListResp;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.demai.cornel.vo.task.GetOrderListResp;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import com.demai.cornel.vo.user.UserLoginSendMsgParam;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Create By zhutf 19-11-10 上午9:33
 */
@Controller @RequestMapping("/supply") @Slf4j public class SupplierCornController {
    @Resource private SupplierTaskService supplierTaskService;

    @Resource private SupplyUserService supplyUserService;

    /***
     * 给用户手机号 发送短信验证码 需要补充逻辑 在n分钟内，发送x条的限制
     * 60s 内最大发3次
     * @param phone 手机号
     * @return
     */
    @RequestMapping(value = "/sendCode.json", method = RequestMethod.POST) @ResponseBody @AccessControl(value = "60_3") public JsonResult userLoginSendCode(
            @RequestBody UserLoginSendMsgParam phone) {
        try {
            log.debug("send code access [{}]", JacksonUtils.obj2String(phone));
            return JsonResult.successStatus(supplyUserService.sendLoginCodeMsg(phone.getPhone()));
        } catch (Exception e) {
            log.error("用户发送短信异常！", e);
            return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
        }
    }

    /**
     * @return
     */
    @RequestMapping(value = "/login.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult doUserLogin(
            @RequestBody UserLoginParam param, HttpServletResponse response) {
        try {
            Preconditions.checkNotNull(param);
            Preconditions.checkNotNull(param.getJscode());
            Preconditions.checkNotNull(param.getPhone());
            Preconditions.checkNotNull(param.getMsgCode());
            UserLoginResp login = supplyUserService.doLogin(param);
            if (login.getCode().compareTo(UserLoginResp.CODE_ENUE.SUCCESS.getValue()) == 0) {
                return JsonResult.success(login);
            } else {
                return JsonResult.successStatus(UserLoginResp.CODE_ENUE.getByValue(login.getCode()));
            }
        } catch (Exception e) {
            log.error("用户登录异常！", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }

    /**
     * 烘干塔列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/shipment-list.json", method = RequestMethod.POST) @ResponseBody public JsonResult shipmentList(
            @RequestBody GetOrderListReq param) {
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
    @RequestMapping(value = "/order-info.json", method = RequestMethod.POST) @ResponseBody public JsonResult orderInfo(
            @RequestBody GetOrderInfoReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            if (param == null || (StringUtil.isEmpty(param.getOrderId()) && StringUtil.isEmpty(param.getVerifyCode()))
                    || ("null".equalsIgnoreCase(param.getOrderId())) && "null"
                    .equalsIgnoreCase(param.getVerifyCode())) {
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
     * @param param
     * @return
     */
    @RequestMapping(value = "/shipment.json", method = RequestMethod.POST) @ResponseBody public JsonResult shipmentStart(
            @RequestBody GetOrderInfoReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            OperationOrderResp result = supplierTaskService.shipmentStart(curUser, param.getOrderId());
            if (log.isDebugEnabled()) {
                log.debug("supplier task order shipment start user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);
        } catch (Exception e) {
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
    @RequestMapping(value = "/shipment-over.json", method = RequestMethod.POST) @ResponseBody public JsonResult shipmentOver(
            @RequestBody OperationOrderReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            OperationOrderResp result = supplierTaskService.shipmentOver(curUser, param);
            if (log.isDebugEnabled()) {
                log.debug("supplier task order shipment over user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);
        } catch (Exception e) {
            log.error("supplier task order shipment over exception！{}", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }

    /**
     * 烘干塔注册接口
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/register.json", method = RequestMethod.POST) @ResponseBody public JsonResult shipmentRegister(
            @RequestBody SupplierRegisterReq param) {
        Preconditions.checkNotNull(param);
        DryTower.REGISTER_STATUS register_status = supplierTaskService.dryTowerRegister(param);
        return JsonResult.successStatus(register_status);
    }

    /**
     * 获取指定人下面的烘干塔信息
     *
     * @return
     */
    @RequestMapping(value = "/get-tower.json", method = RequestMethod.POST) @ResponseBody public JsonResult getDryTowerByUserId() {
        return JsonResult.success(supplierTaskService.getTowerInfoByUserId(CookieAuthUtils.getCurrentUser()));
    }

    /**
     * 根据烘干塔ID 获取烘干塔信息
     *
     * @param orderIdParam
     * @return
     */
    @RequestMapping(value = "/tower-info.json", method = RequestMethod.POST) @ResponseBody public JsonResult getDryTowerInfo(
            @RequestBody String orderIdParam) {
        JSONObject receivedParam = JSON.parseObject(orderIdParam);
        String towerId = (String) receivedParam.get("towerId");
        return JsonResult.success(supplierTaskService.getTowerInfoByTowerId(towerId));
    }

    /**
     * 更新烘干塔信息
     *
     * @param orderIdParam
     * @return
     */
    @RequestMapping(value = "/edit-tower.json", method = RequestMethod.POST) @ResponseBody public JsonResult updateTowerId(
            @RequestBody DryTower orderIdParam) {
        return JsonResult.success(supplierTaskService.updateTowerInfo(orderIdParam));
    }

    /**
     * 增加烘干塔
     *
     * @param orderIdParam
     * @return
     */
    @RequestMapping(value = "/add-tower.json", method = RequestMethod.POST) @ResponseBody public JsonResult addTower(
            @RequestBody AddDryTowerReq orderIdParam) {
        return JsonResult.success(supplierTaskService.adddTowerInfo(orderIdParam));
    }

    /**
     * 烘干塔测点击我的获取个人信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/user-info.json", method = RequestMethod.POST) @ResponseBody public JsonResult addTower() {
        return supplierTaskService.getSupplierInfo();
    }

}
