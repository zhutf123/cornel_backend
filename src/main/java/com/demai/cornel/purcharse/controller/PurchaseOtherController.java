package com.demai.cornel.purcharse.controller;

import com.demai.cornel.purcharse.model.LocationInfo;
import com.demai.cornel.purcharse.service.PurcharseOtherService;
import com.demai.cornel.purcharse.vo.req.AddLocationReq;
import com.demai.cornel.purcharse.vo.req.GetSystemOfferReq;
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
 * @Date: 2020-02-14    20:33
 */
@Controller @RequestMapping("/purchase") @Slf4j public class PurchaseOtherController {

    @Resource private PurcharseOtherService purcharseOtherService;

    /**
     * 增加收货地址
     *
     * @return
     */
    @RequestMapping(value = "/add-location.json", method = RequestMethod.POST) @ResponseBody public JsonResult addLocation(
            @RequestBody AddLocationReq locationInfo) {
        return purcharseOtherService.addLocation(locationInfo);
    }

    /**
     * 获取收货地址list
     * @return
     */
    @RequestMapping(value = "/get-location.json", method = RequestMethod.POST) @ResponseBody public JsonResult getLocation() {
        return purcharseOtherService.getLocationList();
    }

    /**
     * 修改地址
     * @param locationInfo
     * @return
     */
    @RequestMapping(value = "/edit-location.json", method = RequestMethod.POST) @ResponseBody public JsonResult editLocation(
            @RequestBody AddLocationReq locationInfo) {
        return purcharseOtherService.editLocation(locationInfo);
    }

}
