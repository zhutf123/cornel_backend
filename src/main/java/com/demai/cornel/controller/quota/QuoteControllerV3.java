package com.demai.cornel.controller.quota;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.service.QuoteService;
import com.demai.cornel.service.QuoteServiceV3;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.quota.SystemQuoteV2Req;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Author binz.zhang
 * @Date: 2020-03-16    16:23
 */
@Controller @RequestMapping("/quota") @Slf4j
public class QuoteControllerV3 {
    @Resource private QuoteServiceV3 quoteServiceV3;
    @Resource private QuoteService quoteService;

//    @RequestMapping(value = "/click-system-quote.json", method = RequestMethod.POST) @ResponseBody public JsonResult clickOfferSystemQuote(
//            @RequestBody String param) {
//        JSONObject receivedParam = JSON.parseObject(param);
//        String commodityId = (String) receivedParam.get("commodityId");
//        return JsonResult.success(quoteService.getClickInfo(CookieAuthUtils.getCurrentUser(), commodityId));
//    }
    /**
     * 烘干塔接受系统报价的接口 v2
     *
     * @param offerQuoteReq
     * @return
     */
    @RequestMapping(value = "/offer-sysquot-v3.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult offerSystemQuoteV2(@RequestBody SystemQuoteV2Req offerQuoteReq) {
        return JsonResult.success(quoteService.offerSystemQuoteV2(offerQuoteReq));
    }
}
