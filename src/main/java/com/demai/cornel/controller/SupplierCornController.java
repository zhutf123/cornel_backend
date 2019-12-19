/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.service.SupplierTaskService;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.vo.OrderListReq;
import com.demai.cornel.vo.supplier.SupplierTaskListResp;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.service.impl.TaskServiceImp;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.JsonResult;
import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

/**
 * Create By zhutf  19-11-10  上午9:33
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
    @RequestMapping(value = "/shipment-list", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipmentList(@RequestBody OrderListReq param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
            if (StringUtils.isBlank(curUser)) {
                log.error("烘干塔获取任务订单信息失败 用户信息为空");
                return JsonResult.successStatus(ResponseStatusEnum.NO_USER);
            }
            if (param.getStatus() == null) {
                log.error("烘干塔获取任务订单信息失败 查询订单状态为空:{}", curUser);
                return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
            }
            if(log.isDebugEnabled()){
                log.debug("烘干塔查询人物订单列表 user:{} status:{}",curUser,param.getStatus());
            }

            List<SupplierTaskListResp> result = supplierTaskService.getTaskOrderListByStatus(curUser,
                    param.getStatus());

            return JsonResult.success(result);

        } catch (Exception e) {
            log.error("烘干塔获取任务订单信息失败:{}", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }


    /**
     * 烘干塔开始装货
     * @param param
     * @return
     */
    @RequestMapping(value = "/shipment", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipmentConfirm(@RequestBody String param) {
        return null;
    }

    /**
     * 烘干塔装货完成
     * @param param
     * @return
     */
    @RequestMapping(value = "/shipment-over", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipmentOver(@RequestBody String param) {
        return null;
    }
}
