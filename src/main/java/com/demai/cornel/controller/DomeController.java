package com.demai.cornel.controller;

import com.demai.cornel.auth.annotation.Authority;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demai.cornel.vo.JsonResult;

@Controller
@RequestMapping("/test")
@Slf4j
public class DomeController {

    @RequestMapping(value = "/dome", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getProductMainInfoById() {

        return JsonResult.success("success");
    }

    @RequestMapping(value = "/dome2", method = RequestMethod.GET)
    @ResponseBody
    @Authority
    public JsonResult demp2() {
        return JsonResult.success("success");
    }

}
