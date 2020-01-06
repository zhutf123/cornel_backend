package com.demai.cornel.controller.quota;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.service.QuoteService;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.quota.GetQuoteListReq;
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
     * 烘干塔主动报价接口
     *
     * @param offerQuoteReq
     * @return
     */
    @RequestMapping(value = "/offer-quota.json", method = RequestMethod.POST) @ResponseBody public JsonResult offerQuota(
            @RequestBody OfferQuoteReq offerQuoteReq) {
        return JsonResult.success(quoteService.offerQuote(offerQuoteReq));
    }

    /**
     * 烘干塔点击我要报价
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/click-quota.json", method = RequestMethod.POST) @ResponseBody public JsonResult clickQuote() {
        return JsonResult.success(quoteService.clickQuoteRest());
    }

    /**
     * 用户自定义商品
     *
     * @param offerQuoteReq
     * @return
     */
    @RequestMapping(value = "/offer-commodity.json", method = RequestMethod.POST) @ResponseBody public JsonResult offerQuoteList(
            @RequestBody OfferQuoteReq offerQuoteReq) {
        return JsonResult.success(quoteService.clickQuoteRest());
    }

    /**
     * 获取系统报价list
     *
     * @param getQuoteListReq
     * @return
     */
    @RequestMapping(value = "/system-quote.json", method = RequestMethod.POST) @ResponseBody public JsonResult getSystemQuoteList(
            @RequestBody GetQuoteListReq getQuoteListReq) {
        return JsonResult.success(quoteService.getSystemQuoteList(getQuoteListReq));
    }

    /**
     * 烘干塔接受系统报价的接口 todo
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/click-system-quote.json", method = RequestMethod.POST) @ResponseBody public JsonResult clickOfferSystemQuote(
            @RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        String commodityId = (String) receivedParam.get("commodityId");
        return JsonResult.success(quoteService.getClickInfo(CookieAuthUtils.getCurrentUser(), commodityId));
    }

    /**
     * 烘干塔接受系统报价的接口 todo
     *
     * @param offerQuoteReq
     * @return
     */
    @RequestMapping(value = "/offer-system-quote.json", method = RequestMethod.POST) @ResponseBody public JsonResult offerSystemQuote(
            @RequestBody OfferQuoteReq offerQuoteReq) {
        return JsonResult.success(quoteService.offerSystemQuote(offerQuoteReq));
    }

    /**
     * 烘干塔获取议价范围
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/bargain-range.json", method = RequestMethod.POST) @ResponseBody public JsonResult getBargainRange(
            @RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        String commodityId = (String) receivedParam.get("commodityId");
        return JsonResult.success(quoteService.getBargainRange(commodityId));
    }

    /**
     * 获取我主动报价list
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/my-offer.json", method = RequestMethod.POST) @ResponseBody public JsonResult getOwerOfferList(
            @RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        String quoteId = (String) receivedParam.get("quoteId");
        Integer pgSize = (Integer) receivedParam.get("pgSize");
        return JsonResult.success(quoteService.getOfferListRespList(quoteId, pgSize));
    }

    /**
     * 获取被动报价list
     * @param param
     * @return
     */
    @RequestMapping(value = "/my-system-offer.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult getOwerSystemOfferList(
            @RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        String quoteId = (String) receivedParam.get("quoteId");
        Integer pgSize = (Integer) receivedParam.get("pgSize");
        return JsonResult.success(quoteService.getSystemOfferListRespList(quoteId, pgSize));
    }

    /**
     * 获取报价详情
     * @param param
     * @return
     */
    @RequestMapping(value = "/offer-info.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult getOfferInfo(
            @RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        String quoteId = (String) receivedParam.get("quoteId");
        return JsonResult.success(quoteService.getOfferInfoResp(quoteId));
    }
    @RequestMapping(value = "/cancle-offer.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult cancleOffer(@RequestBody String param) {
        JSONObject receivedParam = JSON.parseObject(param);
        String quoteId = (String) receivedParam.get("quoteId");
        return JsonResult.success(quoteService.getOfferInfoResp(quoteId));
    }
}
