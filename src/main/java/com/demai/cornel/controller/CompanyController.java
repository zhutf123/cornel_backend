/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.controller;

import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.service.CompanyService;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.user.CompanyParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Create By tfzhu  2020/12/27  11:49 AM
 *
 * @author tfzhu
 */
@Controller @RequestMapping("/company") @Slf4j public class CompanyController {

    @Resource private CompanyService companyService;

    /***
     * 添加公司信息， 返回注册id
     * @return
     */
    @RequestMapping(value = "/add.json", method = RequestMethod.POST) @ResponseBody public JsonResult addCompany(
            @RequestBody CompanyParam param) {
        try {
            String curUser = Optional.ofNullable(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)).orElse(null);
            if (param.getUserId().equals(curUser)) {
                return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
            }

            String companyId = companyService.addCompanyInfo(param);
            log.debug("add company success  company name={},companyId is {}", param.getCompanyName(), companyId);
            return JsonResult.success(companyId);
        } catch (Exception e) {
            log.error("添加公司信息异常！", e);
            return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
        }
    }
}
