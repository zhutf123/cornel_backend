/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.service.TaskService;
import com.demai.cornel.service.impl.TaskServiceImp;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.user.UserLoginResp;
import com.demai.cornel.vo.user.UserLoginSendMsgParam;
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
@Controller
@RequestMapping("/driver")
@Slf4j
public class DriverOperationController {
    @Resource
    private TaskServiceImp taskServiceImp;

    @RequestMapping(value = "/task-list", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getTaskList(@RequestBody String param) {
        if (Strings.isNullOrEmpty(param)){
            return JsonResult.error("param illegal");
        }
        JSONObject receivedParam = JSON.parseObject(param);
        Integer pgSize = (Integer) receivedParam.get("pgSize");
        Integer curId = (Integer) receivedParam.get("curId");
        if (pgSize == null) pgSize = 20;
        String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse("UNKNOW_");
        return JsonResult.success(taskServiceImp.getDistTaskList(curUser,curId,pgSize));
    }
}
