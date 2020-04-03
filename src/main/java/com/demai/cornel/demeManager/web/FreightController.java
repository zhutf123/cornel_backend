package com.demai.cornel.demeManager.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.demeManager.service.AdminFreightService;
import com.demai.cornel.demeManager.service.AdminPurchaseCornService;
import com.demai.cornel.demeManager.service.AdminSaleOrderService;
import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.vo.JsonResult;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
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
    @RequestMapping(value = "/get-freight-view.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult adminGetSysOfferList(
            @RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        Integer pgSize = (Integer) receivedParam.get("pgSize");
        Integer offSet = (Integer) receivedParam.get("offSet");
        return JsonResult.success(adminFreightService.adminGetFreightView(offSet, pgSize));
    }

    /**
     * 管理员获取买家系统的报价list
     *
     * @return
     */
    @RequestMapping(value = "/get-location-list.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult adminGetLocationList(
            @RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        Integer pgSize = (Integer) receivedParam.get("pgSize");
        Integer offSet = (Integer) receivedParam.get("offSet");
        return JsonResult.success(adminFreightService.getSystemLocationList(offSet, pgSize));
    }

    /**
     * 管理员获取买家系统的报价list
     *
     * @return
     */
    @RequestMapping(value = "/add-location.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult adminGetLocationList(
            @RequestBody AdminLocationMode param) {
        Preconditions.checkNotNull(param);
        return JsonResult.success(adminFreightService.adminAddLocation(param));
    }

    /**
     * 管理员获取买家系统的报价list
     *
     * @return
     */
    @RequestMapping(value = "/get-transport-type.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult adminGetTransportType() {
        return JsonResult.success(adminFreightService.getTransport());
    }

        @RequestMapping(value = "/get-freight-type.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult adminGetFreightType() {
        return JsonResult.success(adminFreightService.getFreightInfo());
    }

    @RequestMapping(value = "/get-freight-detail.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult adminGetFreightInfo(

            @RequestBody @Param("towerId") String towerId) {
        return JsonResult.success(adminFreightService.adminGetFreight(towerId));
    }

    @RequestMapping(value = "/update-freight-info.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult adminUpdateFreightInfo(
            @RequestBody AdminUpdateFreightReq adminUpdateFreightReq) {
        return JsonResult.success(adminFreightService.updateFreightInfo(adminUpdateFreightReq));
    }

}
