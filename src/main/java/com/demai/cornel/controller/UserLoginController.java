/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import com.demai.cornel.vo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Create By zhutf 19-10-31 上午10:43
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserLoginController {

    /***
     * 给用户手机号 发送短信验证码 需要补充逻辑 在n分钟内，发送x条的限制
     * 
     * @param phone
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getProductMainInfoById(String phone) {
        return JsonResult.success("succ");
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getProductMainInfoById() {
        return JsonResult.success("");
    }

}
