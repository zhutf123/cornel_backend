package com.demai.cornel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.service.AgreementService;
import com.demai.cornel.vo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Author binz.zhang
 * @Date: 2020-01-08    17:16
 */
@Controller @RequestMapping("/agreement") @Slf4j public class AgreementController {
    @Resource private AgreementService agreementService;


    @RequestMapping(value = "/get-agree.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult getAgree(@RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        String quoteId = (String) receivedParam.get("adapt");
        return JsonResult.success(agreementService.getAgreementByAdapt(quoteId));
    }

}
