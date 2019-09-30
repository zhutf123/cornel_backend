package com.demai.cornel.controller;

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
public class TestController extends SpringBootServletInitializer {

    @RequestMapping(value = "/dome", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getProductMainInfoById() {

        return JsonResult.success("success");
    }

    /**
     * 需要把web项目打成war包部署到外部tomcat运行时需要改变启动方式
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TestController.class);
    }

}
