package com.demai.cornel.demeManager.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.demeManager.service.AdminFreightService;
import com.demai.cornel.demeManager.service.AdminPurchaseCornService;
import com.demai.cornel.demeManager.service.AdminSaleOrderService;
import com.demai.cornel.demeManager.vo.AdminEditSysOfferReq;
import com.demai.cornel.demeManager.vo.AdminGetBuyerReq;
import com.demai.cornel.demeManager.vo.AdminReviewPayReq;
import com.demai.cornel.demeManager.vo.AdminReviewSaleReq;
import com.demai.cornel.vo.JsonResult;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    12:06
 */
@Controller @RequestMapping("/admin/freight") @Slf4j public class FreightController {

    @Resource private AdminPurchaseCornService adminPurchaseCornService;
    @Resource private AdminFreightService adminFreightService;

    /**
     * 管理员获取买家系统的报价list
     *
     * @return
     */
    @RequestMapping(value = "/get-freight-view.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody public JsonResult adminGetSysOfferList(@RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        Integer pgSize = (Integer) receivedParam.get("pgSize");
        Integer offSet = (Integer) receivedParam.get("offSet");
        return JsonResult.success(adminFreightService.adminGetFreightView(offSet,pgSize));
    }


}
