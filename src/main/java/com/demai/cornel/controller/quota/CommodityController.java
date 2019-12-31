package com.demai.cornel.controller.quota;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.service.CommodityService;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.commodity.CommodityResp;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @Author binz.zhang
 * @Date: 2019-12-27    16:48
 */
@Controller @RequestMapping("/commodity") @Slf4j public class CommodityController {
    @Resource private CommodityService commodityService;

    @RequestMapping(value = "/commodity-list.json", method = RequestMethod.POST) @ResponseBody public JsonResult getCommodityList() {
        String userId = UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME);
        List<Commodity> commodityList = commodityService.getAllCommodity(userId);
        return JsonResult.success(commodityList);
    }

    /**
     * 用户上传自定义商品
     *
     * @param commodity
     * @return
     */
    @RequestMapping(value = "/upload-commodity.json", method = RequestMethod.POST) @ResponseBody public JsonResult uploadCommodity(
            @RequestBody Commodity commodity) {
        if (commodityService.insertCommodity(commodity)) {
            return JsonResult.success(CommodityResp.builder().commodityInfo(commodity).status(0).build());
        }
        return JsonResult.success(CommodityResp.builder().status(1).build());
    }


}
