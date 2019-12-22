/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.TaskInfoReq;
import com.demai.cornel.service.OrderService;
import com.demai.cornel.service.SupplierTaskService;
import com.demai.cornel.service.UserLoginService;
import com.demai.cornel.service.impl.TaskServiceImp;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.task.TaskSaveVO;
import com.google.common.base.Preconditions;
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
@Controller
@RequestMapping("/task")
@Slf4j
public class TaskOperationController {
    @Resource private TaskServiceImp taskServiceImp;


    @Resource private OrderService orderService;


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
}
