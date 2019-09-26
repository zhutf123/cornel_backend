package com.demai.cornal.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demai.cornal.vo.JsonResult;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping(value = "/dome", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getProductMainInfoById() {

        return JsonResult.success("success");
    }

}
