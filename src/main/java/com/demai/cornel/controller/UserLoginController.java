/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import com.demai.cornel.service.UserLoginService;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import com.google.common.base.Preconditions;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.CheckForNull;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create By zhutf 19-10-31 上午10:43
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserLoginController {

    @Resource
    private UserLoginService userLoginService;

    /***
     * 给用户手机号 发送短信验证码 需要补充逻辑 在n分钟内，发送x条的限制
     *
     * @param p 手机号
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getProductMainInfoById(String p) {
        try {
            userLoginService.sendLoginCodeMsg(p);
            return JsonResult.success("");
        } catch (Exception e) {
            log.error("用户发送短信异常！", e);
            return JsonResult.error("网络异常，请稍后重试！");
        }
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public JsonResult getProductMainInfoById(@RequestBody UserLoginParam param, HttpServletResponse response) {
        try {
            Preconditions.checkNotNull(param);
            Preconditions.checkNotNull(param.getJscode());
            Preconditions.checkNotNull(param.getPhone());
            Preconditions.checkNotNull(param.getMsgCode());

            UserLoginResp login = userLoginService.doLogin(param);
            if (login != null) {
                return JsonResult.success(login);
            }
            return JsonResult.error("网络异常，请稍后重试！");
        } catch (Exception e) {
            log.error("用户登录异常！", e);
            return JsonResult.error("网络异常，请稍后重试！");
        }

    }

}
