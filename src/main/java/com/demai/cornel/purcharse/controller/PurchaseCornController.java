package com.demai.cornel.purcharse.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.annotation.AccessControl;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.purcharse.service.PurchaseCornService;
import com.demai.cornel.purcharse.vo.req.GetSystemOfferReq;
import com.demai.cornel.purcharse.vo.req.SystemOfferReq;
import com.demai.cornel.service.UserLoginService;
import com.demai.cornel.service.UserService;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.user.UserLoginSendMsgParam;
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
 * @Date: 2020-02-12    14:16
 */

@Controller
@RequestMapping("/purchase")
@Slf4j
public class PurchaseCornController {
    @Resource
    private PurchaseCornService purchaseCornService;
    @Resource
    private UserService userService;

    /**
     * 获取系统报价
     * @return
     */
    @RequestMapping(value = "/get-system-offer.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult systemOffer(@RequestBody GetSystemOfferReq getSystemOfferReq) {
            return JsonResult.success(purchaseCornService.getSystemOfferRespList(getSystemOfferReq));
    }
    /**
     * 获取系统报价
     * @return
     */
    @RequestMapping(value = "/click-system-offer.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult clickSystemOffe(@RequestBody String param) {
        Preconditions.checkNotNull(param);
        JSONObject receivedParam = JSON.parseObject(param);
        String orderId = (String) receivedParam.get("offerId");
        return JsonResult.success(purchaseCornService.clickSystemQuoteResp(orderId));
    }

    /**
     * 点击我要报价 系统补充信息
     * @return
     */
    @RequestMapping(value = "/click-my-offer.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult clickSystemOffe() {
        return JsonResult.success(purchaseCornService.clickMyOfferResp());
    }

    /**
     * 买家下单系统商品操作
     * @return
     */
    @RequestMapping(value = "/submit-system-offer.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult systemOffer(@RequestBody SystemOfferReq param) {
        return JsonResult.success(purchaseCornService.submitSystemQuoteResp(param));
    }



}
