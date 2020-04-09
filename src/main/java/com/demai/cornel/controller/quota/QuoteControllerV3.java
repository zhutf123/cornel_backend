package com.demai.cornel.controller.quota;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.service.QuoteService;
import com.demai.cornel.service.QuoteServiceV3;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.quota.CalculateDryReq;
import com.demai.cornel.vo.quota.ConfirmOrderReq;
import com.demai.cornel.vo.quota.GetQuotePriceRep;
import com.demai.cornel.vo.quota.SystemQuoteV2Req;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * @Author binz.zhang
 * @Date: 2020-03-16    16:23
 */
@Controller @RequestMapping("/quota") @Slf4j
public class QuoteControllerV3 {
    @Resource private QuoteServiceV3 quoteServiceV3;
    @Resource private QuoteService quoteService;

    /**
     * 烘干塔接受系统报价的接口 v2
     *
     * @param offerQuoteReq
     * @return
     */
    @RequestMapping(value = "/offer-sysquot-v3.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult offerSystemQuoteV2(@RequestBody SystemQuoteV2Req offerQuoteReq) {
        return JsonResult.success(quoteServiceV3.offerSystemQuoteV2(offerQuoteReq));
    }

    @RequestMapping(value = "/edit-sysquot-v3.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult editSystemQuote(@RequestBody SystemQuoteV2Req offerQuoteReq) {
        return JsonResult.success(quoteServiceV3.editSystemQuote(offerQuoteReq));
    }

    @RequestMapping(value = "/get-quote-price.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult getQuotePrice(@RequestBody GetQuotePriceRep getQuotePriceRep)
            throws ParseException {
        return JsonResult.success(quoteServiceV3.getQuotePrice(getQuotePriceRep));
    }
    @RequestMapping(value = "/get-wetdry-radio.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult getDryWetRadio() {
        return JsonResult.success(quoteServiceV3.getDryWetRadio());
    }

    @RequestMapping(value = "/calculate-dry.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult calculateDryWeight(@RequestBody CalculateDryReq calculateDryReq) {
        return JsonResult.success(quoteServiceV3.calculateDryWeight(calculateDryReq));
    }
    @RequestMapping(value = "/confirm-order.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult confirmOrder(@RequestBody ConfirmOrderReq confirmOrderReq) {
        return JsonResult.success(quoteServiceV3.confirmOrder(confirmOrderReq));
    }
}
