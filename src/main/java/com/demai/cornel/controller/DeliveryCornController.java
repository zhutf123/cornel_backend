/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import java.util.Optional;

import javax.annotation.Resource;

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
@RequestMapping("/delivery")
@Slf4j
public class DeliveryCornController {
    @Resource
    private TaskServiceImp taskServiceImp;

    /**
     * 接货人列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deliveryList(@RequestBody String param) {
        if (Strings.isNullOrEmpty(param)) {
            return JsonResult.error("param illegal");
        }
        JSONObject receivedParam = JSON.parseObject(param);
        Integer pgSize = (Integer) receivedParam.get("pgSize");
        Integer curId = (Integer) receivedParam.get("curId");
        if (pgSize == null) pgSize = 20;
        if (curId <= 0) {
            curId = null;
        }
        String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
        return JsonResult.success(taskServiceImp.getDistTaskList(curUser, curId, pgSize));
    }


    /**
     * 接货人确认 开始卸货
     * @param param
     * @return
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deliveryConfirm(@RequestBody String param) {
        if (Strings.isNullOrEmpty(param)) {
            return JsonResult.error("param illegal");
        }
        JSONObject receivedParam = JSON.parseObject(param);
        Integer pgSize = (Integer) receivedParam.get("pgSize");
        Integer curId = (Integer) receivedParam.get("curId");
        if (pgSize == null) pgSize = 20;
        if (curId <= 0) {
            curId = null;
        }
        String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
        return JsonResult.success(taskServiceImp.getDistTaskList(curUser, curId, pgSize));
    }

    /**
     * 卸货完成
     * @param param
     * @return
     */
    @RequestMapping(value = "/over", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deliveryOver(@RequestBody String param) {
        if (Strings.isNullOrEmpty(param)) {
            return JsonResult.error("param illegal");
        }
        JSONObject receivedParam = JSON.parseObject(param);
        Integer pgSize = (Integer) receivedParam.get("pgSize");
        Integer curId = (Integer) receivedParam.get("curId");
        if (pgSize == null) pgSize = 20;
        if (curId <= 0) {
            curId = null;
        }
        String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
        return JsonResult.success(taskServiceImp.getDistTaskList(curUser, curId, pgSize));
    }
}
