/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import com.demai.cornel.business.UserLoginBusiness;
import com.demai.cornel.vo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Create By zhutf 19-10-31 上午10:43
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserLoginController {

    @Resource
    private UserLoginBusiness userLoginBusiness;

    /***
     * 给用户手机号 发送短信验证码 需要补充逻辑 在n分钟内，发送x条的限制
     *
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getProductMainInfoById(String phoneNum) {
        try {

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
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getProductMainInfoById() {
        try {
            // userLoginBusiness.

            return JsonResult.success("");
        } catch (Exception e) {
            log.error("用户登录异常！", e);
            return JsonResult.error("网络异常，请稍后重试！");
        }

    }

}
