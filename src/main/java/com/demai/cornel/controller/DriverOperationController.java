/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.model.TaskInfo;
import com.demai.cornel.model.TaskInfoReq;
import com.demai.cornel.service.OrderService;
import com.demai.cornel.service.UserLoginService;
import com.demai.cornel.service.impl.TaskServiceImp;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.task.ArriveDepDriverResp;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.demai.cornel.vo.task.GetOrderListResp;
import com.demai.cornel.vo.task.TaskSaveVO;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Create By zhutf  19-11-10  上午9:33
 */
@Controller @RequestMapping("/driver") @Slf4j public class DriverOperationController {
    @Resource private TaskServiceImp taskServiceImp;
    @Resource private UserLoginService userLoginService;

    @Resource private OrderInfoDao orderInfoDao;

    @Resource private OrderService orderService;

    @RequestMapping(value = "/task-list.json", method = RequestMethod.POST) @ResponseBody public JsonResult getTaskList(
            @RequestBody String param) {
        if (Strings.isNullOrEmpty(param)) {
            return JsonResult.error("param illegal");
        }
        JSONObject receivedParam = JSON.parseObject(param);
        Integer pgSize = (Integer) receivedParam.get("pgSize");
        Integer curId = (Integer) receivedParam.get("curId");
        if (pgSize == null)
            pgSize = 20;
        if (curId != null && curId <= 0) {
            curId = null;
        }
        String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
        return JsonResult.success(taskServiceImp.getDistTaskList(curUser, curId, pgSize));
    }

    @RequestMapping(value = "/info.json", method = RequestMethod.POST) @ResponseBody public JsonResult getTaskInfo(
            @RequestBody String taskIdParam) {
        Preconditions.checkNotNull(taskIdParam);
        JSONObject receivedParam = JSON.parseObject(taskIdParam);
        String taskId = (String) receivedParam.get("taskId");
        String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
        TaskInfoReq taskInfoReq = taskServiceImp.getTaskInfo(curUser, taskId);
        if (taskInfoReq == null) {
            JsonResult.error("error re");
        }
        return JsonResult.success(taskInfoReq);
    }

    @RequestMapping(value = "/save.json", method = RequestMethod.POST) @ResponseBody public JsonResult saveTask(
            @RequestBody TaskSaveVO taskSaveVO) {
        Preconditions.checkNotNull(taskSaveVO);
        return orderService.saveOrder(taskSaveVO);
    }

    @RequestMapping(value = "/order-list.json", method = RequestMethod.POST) @ResponseBody public JsonResult getOrderList(
            @RequestBody GetOrderListReq getOrderListReq) {
        Preconditions.checkNotNull(getOrderListReq);
        if (Strings.isNullOrEmpty(getOrderListReq.getOrderId()) || getOrderListReq.getOrderTyp() == null) {
            return JsonResult.successStatus(GetOrderListResp.CODE_ENUE.PARAM_ERROR);
        }
        if (userLoginService.checkUserValidity(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)) == null) {
            return JsonResult.successStatus(GetOrderListResp.CODE_ENUE.PARAM_ERROR);
        }
        return null;
    }

    @RequestMapping(value = "/order-info.json", method = RequestMethod.POST) @ResponseBody public JsonResult getOrderInfo(
            @RequestBody String orderIdParam) {
        Preconditions.checkNotNull(orderIdParam);
        JSONObject receivedParam = JSON.parseObject(orderIdParam);
        String orderId = (String) receivedParam.get("orderId");
        return JsonResult.success(
                orderInfoDao.getOrderInfoByUserAndOrderId(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME), orderId));
    }

    @RequestMapping(value = "/arrive-dep.json", method = RequestMethod.POST) @ResponseBody public JsonResult arriveDep(
            @RequestBody String orderIdParam) {
        Preconditions.checkNotNull(orderIdParam);
        JSONObject receivedParam = JSON.parseObject(orderIdParam);
        String orderId = (String) receivedParam.get("orderId");
        return JsonResult.success(orderService.driverArriveDep(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME),orderId));
    }
    @RequestMapping(value = "/confirm-stockOut.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult confirmStockOut(
            @RequestBody String orderIdParam) {
        Preconditions.checkNotNull(orderIdParam);
        JSONObject receivedParam = JSON.parseObject(orderIdParam);
        String orderId = (String) receivedParam.get("orderId");
        return JsonResult.success(orderService.confirmStockOut(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME),orderId));
    }

    @RequestMapping(value = "/arrive-arr.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult arriveArr(
            @RequestBody String orderIdParam) {
        Preconditions.checkNotNull(orderIdParam);
        JSONObject receivedParam = JSON.parseObject(orderIdParam);
        String orderId = (String) receivedParam.get("orderId");
        return JsonResult.success(orderService.arriveArr(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME),orderId));
    }
}
