/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.demai.cornel.annotation.AccessControl;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.service.UserService;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.user.UserAddReq;
import com.demai.cornel.vo.user.UserLoginSendMsgParam;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demai.cornel.service.UserLoginService;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import com.google.common.base.Preconditions;

/**
 * Create By zhutf 19-10-31 上午10:43
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserLoginController {

    @Resource
    private UserLoginService userLoginService;
    @Resource
    private UserService userService;

    /***
     * 给用户手机号 发送短信验证码 需要补充逻辑 在n分钟内，发送x条的限制
     * 60s 内最大发3次
     * @param phone 手机号
     * @return
     */
    @RequestMapping(value = "/sendCode.json", method = RequestMethod.POST)
    @ResponseBody
    @AccessControl(value = "60_3")
    public JsonResult userLoginSendCode(@RequestBody UserLoginSendMsgParam phone) {
        try {
            log.debug("send code access [{}]", JacksonUtils.obj2String(phone));
            return JsonResult.successStatus(userLoginService.sendLoginCodeMsg(phone.getPhone()));
        } catch (Exception e) {
            log.error("用户发送短信异常！", e);
            return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
        }
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/login.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public JsonResult doUserLogin(@RequestBody UserLoginParam param, HttpServletResponse response) {
        try {
            Preconditions.checkNotNull(param);
            Preconditions.checkNotNull(param.getJscode());
            Preconditions.checkNotNull(param.getPhone());
            Preconditions.checkNotNull(param.getMsgCode());
            UserLoginResp login = userLoginService.doLogin(param);
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

    @RequestMapping(value = "/update-user.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public JsonResult addUser(@RequestBody UserAddReq param) {
        try {
            Preconditions.checkNotNull(param);
            return JsonResult.success(userService.updateUserInfo(param));
        } catch (Exception e) {
            log.error("用户登录异常！", e);
        }
        return JsonResult.success(ResponseStatusEnum.NETWORK_ERROR);
    }

    @RequestMapping(value = "/get-user.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public JsonResult getUserInfo() {
        try {
            return JsonResult.success(userService.getUserInfoResp());
        } catch (Exception e) {
            log.error("用户登录异常！", e);
        }
        return JsonResult.success(ResponseStatusEnum.NETWORK_ERROR);
    }

}
