package com.demai.cornel.controller.quota;

import com.demai.cornel.service.QuoteService;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.quota.OfferQuoteReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Author binz.zhang
 * @Date: 2019-12-31    12:14
 * 报价接口
 */
@Controller @RequestMapping("/quota") @Slf4j public class QuoteController {
    @Resource private QuoteService quoteService;

    /**
     * 烘干塔报价接口
     *
     * @param offerQuoteReq
     * @return
     */
    @RequestMapping(value = "/offer-quota.json", method = RequestMethod.POST) @ResponseBody public JsonResult offerQuota(
            @RequestBody OfferQuoteReq offerQuoteReq) {
        return JsonResult.success(quoteService.offerQuote(offerQuoteReq));
    }


    /**
     * 烘干塔报价接口
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/click-quota.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult clickQuote() {
        return JsonResult.success(quoteService.clickQuoteRest());
    }
}
