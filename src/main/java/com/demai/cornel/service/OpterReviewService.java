package com.demai.cornel.service;

import com.demai.cornel.model.ReviewModel;
import com.demai.cornel.model.ReviewOptResp;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author binz.zhang
 * @Date: 2020-03-23    17:03
 */
@Service
@Slf4j
public class OpterReviewService {

    /**
     *  转换烘干塔的提交订单的审批意见给小程序客户端
     * @param optReview
     * @return
     */
   public ReviewOptResp towerReviewConvert(HashMap<String,String>optReview){
       if(optReview==null){
          return  ReviewOptResp.builder().detailDesc("").errTag(Collections.EMPTY_LIST).build();
       }
       Integer errCode  = 0;
       try {
           errCode  = Integer.valueOf(optReview.get("errCode"));
       }catch (Exception e){
           log.error("parse review err code err code is {}",optReview.get("errCode"));
       }
       String desc = Strings.isNullOrEmpty(optReview.get("errDesc")) ? "": optReview.get("errDesc");
       if(errCode==null ){
           return ReviewOptResp.builder().detailDesc(desc).errTag(Collections.EMPTY_LIST).build();
       }
       List<ReviewModel> errTag = new LinkedList<>();
       Integer finalErrCode = errCode;
       Arrays.stream(ReviewModel.TOWER_SUP_ORDER_ERR.values()).forEach(x->{
           if(x.getValue()==(x.getValue()& finalErrCode)){
               ReviewModel reviewModel = new ReviewModel();
               reviewModel.setErrCode(x.getValue());
               reviewModel.setDesc(x.getExpr());
               errTag.add(reviewModel);
           }
       });
      return ReviewOptResp.builder().detailDesc(desc).errTag(errTag).build();
   }

}
