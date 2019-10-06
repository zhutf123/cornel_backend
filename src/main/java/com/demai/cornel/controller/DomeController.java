package com.demai.cornel.controller;

import com.demai.cornel.constant.ConfigProperties;
import com.demai.cornel.service.WeChatService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demai.cornel.vo.JsonResult;

import javax.annotation.Resource;

@Controller
@RequestMapping("/test")
@Slf4j
public class DomeController {

    @Resource
    private ConfigProperties configProperties;
    @Resource
    private WeChatService weChatService;

    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getProductMainInfoById() {
        return JsonResult.success(configProperties.weChatCode2SessionPath);
    }

    @RequestMapping(value = "/demo_get", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult dealGet() {
        weChatService.code2Session();
        return JsonResult.success("");
    }

}
