/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Resource;

import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.service.DeliveryTaskService;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.supplier.SupplierTaskListResp;
import com.demai.cornel.vo.task.GetOrderListReq;
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
            if (param.getOrderTyp() == null) {
                log.error("接货人获取任务订单信息失败 查询订单状态为空:{}", curUser);
                return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
            }
            if (log.isDebugEnabled()) {
                log.debug("接货人查询任务订单列表 user:{} status:{}", curUser, param);
            }
            Collection<SupplierTaskListResp> result = deliveryTaskService.getTaskOrderListByStatus(curUser, param);
            if (log.isDebugEnabled()) {
                log.debug("接货人查询任务订单列表 user:{} result:{}", curUser, JsonUtil.toJson(result));
            }
            return JsonResult.success(result);

        } catch (Exception e) {
            log.error("接货人查询任务订单信息失败:{}", e);
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
    public JsonResult deliveryConfirm(@RequestBody String param) {
        return null;
    }

    /**
     * 卸货完成
     * @param param
     * @return
     */
    @RequestMapping(value = "/over.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deliveryOver(@RequestBody String param) {
        return null;
    }
}
