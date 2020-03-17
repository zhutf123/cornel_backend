package com.demai.cornel.service;

import com.demai.cornel.dao.ImgInfoDao;
import com.demai.cornel.model.ImgInfo;
import com.demai.cornel.model.ImgInfoReq;
import com.demai.cornel.util.JacksonUtils;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    17:23
 */
@Service @Slf4j public class ImgService {
    @Resource private ImgInfoDao imgInfoDao;

    public boolean saveUserInfoImg(ImgInfoReq imgInfoReq, String userId) {
        if (imgInfoReq == null || Strings.isNullOrEmpty(userId)) {
            return false;
        }
        try {
            ImgInfo imgInfo = new ImgInfo();
            imgInfo.setStatus(ImgInfo.STATUS.EXIST.getValue());
            imgInfo.setBindType(ImgInfo.BINDTYPESTATUS.BIND_USER.getValue());
            imgInfo.setBindId(userId);
            imgInfo.setImgId(UUID.randomUUID().toString());
            imgInfo.setImgDesc(ImgInfo.IMGDESC.keyOf(imgInfoReq.getKey()).getExpr());
            imgInfo.setUrl(imgInfoReq.getUrl());
            int res = imgInfoDao.insert(imgInfo);
            if (res == 0) {
                log.info("insert img into db fail img info is [{}]", JacksonUtils.obj2String(imgInfo));
                return false;
            }
        } catch (Exception e) {
            log.error("insert img into db error", e);
            return false;
        }
        return true;
    }

    public boolean saveCarInfoImg(ImgInfoReq imgInfoReq, String lorryId) {
        if (imgInfoReq == null || Strings.isNullOrEmpty(lorryId)) {
            return false;
        }
        try {
            ImgInfo imgInfo = new ImgInfo();
            imgInfo.setStatus(ImgInfo.STATUS.EXIST.getValue());
            imgInfo.setBindType(ImgInfo.BINDTYPESTATUS.BIND_CAR.getValue());
            imgInfo.setBindId(lorryId);
            imgInfo.setImgId(UUID.randomUUID().toString());
            imgInfo.setImgDesc(ImgInfo.IMGDESC.keyOf(imgInfoReq.getKey()).getExpr());
            imgInfo.setUrl(imgInfoReq.getUrl());
            int res = imgInfoDao.insert(imgInfo);
            if (res == 0) {
                log.info("insert img into db fail img info is [{}]", JacksonUtils.obj2String(imgInfo));
                return false;
            }
        } catch (Exception e) {
            log.error("insert img into db error", e);
            return false;
        }
        return true;
    }
    public boolean saveQuoteInfoImg(ImgInfoReq imgInfoReq, String quoteId) {
        if (imgInfoReq == null || Strings.isNullOrEmpty(quoteId)) {
            return false;
        }
        try {
            ImgInfo imgInfo = new ImgInfo();
            imgInfo.setStatus(ImgInfo.STATUS.EXIST.getValue());
            imgInfo.setBindType(ImgInfo.BINDTYPESTATUS.BIND_QUOTE.getValue());
            imgInfo.setBindId(quoteId);
            imgInfo.setImgId(UUID.randomUUID().toString());

            if(!Strings.isNullOrEmpty(imgInfoReq.getKey()) && imgInfoReq.getKey().startsWith(ImgInfo.IMGDESC.QUOTE_ATTACH.getKey())){
                String descTem = (ImgInfo.IMGDESC.QUOTE_ATTACH.getExpr());
                List<String> attSuff = Splitter.on("_").splitToList(imgInfoReq.getKey());
                if(attSuff!=null&&attSuff.size()>=2){
                    descTem = descTem+attSuff.get(2);
                }
                imgInfo.setImgDesc(descTem);
            }else {
                imgInfo.setImgDesc(ImgInfo.IMGDESC.keyOf(imgInfoReq.getKey()).getExpr());
            }
            imgInfo.setUrl(imgInfoReq.getUrl());
            int res = imgInfoDao.insert(imgInfo);
            if (res == 0) {
                log.info("insert img into db fail img info is [{}]", JacksonUtils.obj2String(imgInfo));
                return false;
            }
        } catch (Exception e) {
            log.error("insert img into db error", e);
            return false;
        }
        return true;
    }



    public void updateQuoteImg(List<ImgInfoReq> imgInfoReqs,String quoteId){
        List<ImgInfo> oldImgInfo = imgInfoDao.getUserImgByQuoteId(quoteId);
        if(oldImgInfo==null){
            saveQuoteImgs(imgInfoReqs,quoteId);
            return;
        }
        imgInfoReqs.stream().forEach(x->{
            String desc="";
            if(!Strings.isNullOrEmpty(x.getKey()) && x.getKey().startsWith(ImgInfo.IMGDESC.QUOTE_ATTACH.getKey())){
                desc = (ImgInfo.IMGDESC.QUOTE_ATTACH.getExpr());
                List<String> attSuff = Splitter.on("_").splitToList(x.getKey());
                if(attSuff!=null&&attSuff.size()>=2){
                    desc = desc+"_"+attSuff.get(2);
                }
            }else {
                desc  = (ImgInfo.IMGDESC.keyOf(x.getKey()).getExpr());
            }
            for (ImgInfo imgInfo:oldImgInfo) {
                if(!Strings.isNullOrEmpty(imgInfo.getImgDesc()) && imgInfo.getImgDesc().equals(desc)){
                    imgInfoDao.updateImgUrl(quoteId,desc,x.getUrl());
                }
            }

        });



    }

    public List<ImgInfoReq> getCarImgs(String lorryId){
        List<ImgInfo> imgInfos = imgInfoDao.getCarImgByLorryId(lorryId);
        if(imgInfos==null || imgInfos.size()<=0){
            return Collections.EMPTY_LIST;
        }
        List<ImgInfoReq> reqs = new ArrayList<>();
        imgInfos.stream().forEach(x->{
            ImgInfo.IMGDESC imgdesc = ImgInfo.IMGDESC.exparOf(x.getImgDesc());
            if(imgdesc!=null){
                reqs.add(new ImgInfoReq(imgdesc.getKey(),x.getUrl()));
            }
        });
        return reqs;
    }


    public List<ImgInfoReq> getQuoteImgs(String quoteId){
        List<ImgInfo> imgInfos = imgInfoDao.getUserImgByQuoteId(quoteId);
        if(imgInfos==null || imgInfos.size()<=0){
            return Collections.EMPTY_LIST;
        }
        List<ImgInfoReq> reqs = new ArrayList<>();
        imgInfos.stream().forEach(x->{
            String imgKey = null;
            if(x.getImgDesc().startsWith(ImgInfo.IMGDESC.QUOTE_ATTACH.getExpr())){
                 imgKey = ImgInfo.IMGDESC.QUOTE_ATTACH.getKey();
                List<String> attmenchSup = Splitter.on("_").splitToList(x.getImgDesc());
                if(attmenchSup!=null&&attmenchSup.size()>=2){
                    imgKey = imgKey+"_"+attmenchSup.get(2);
                }
            }else {
                imgKey = ImgInfo.IMGDESC.exparOf(x.getImgDesc()).getKey();
            }
            if(imgKey!=null){
                reqs.add(new ImgInfoReq(imgKey,x.getUrl()));
            }
        });

        return reqs;
    }
    public List<ImgInfoReq> getUserImgs(String userId){
        List<ImgInfo> imgInfos = imgInfoDao.getUserImgByUserId(userId);
        if(imgInfos==null || imgInfos.size()<=0){
            return Collections.EMPTY_LIST;
        }
        List<ImgInfoReq> reqs = new ArrayList<>();
        imgInfos.stream().forEach(x->{
            ImgInfo.IMGDESC imgdesc = ImgInfo.IMGDESC.exparOf(x.getImgDesc());
            if(imgdesc!=null){
                reqs.add(new ImgInfoReq(imgdesc.getKey(),x.getUrl()));
            }
        });
        return reqs;
    }

    public boolean saveUserInfoImgs(List<ImgInfoReq> imgInfoReqs, String userId) {
        if (imgInfoReqs == null || Strings.isNullOrEmpty(userId)) {
            return false;
        }
        for (ImgInfoReq imgInfoReq : imgInfoReqs) {
            if (!saveUserInfoImg(imgInfoReq, userId)) {
                return false;
            }
        }
        return true;
    }

    public boolean saveCarInfoImgs(List<ImgInfoReq> imgInfoReqs, String lorryId) {
        if (imgInfoReqs == null || Strings.isNullOrEmpty(lorryId)) {
            return false;
        }
        for (ImgInfoReq imgInfoReq : imgInfoReqs) {
            if (!saveCarInfoImg(imgInfoReq, lorryId)) {
                return false;
            }
        }
        return true;
    }

    public boolean saveQuoteImgs(List<ImgInfoReq> imgInfoReqs, String quoteId) {
        if (imgInfoReqs == null || Strings.isNullOrEmpty(quoteId)) {
            return false;
        }
        for (ImgInfoReq imgInfoReq : imgInfoReqs) {
            if (!saveQuoteInfoImg(imgInfoReq, quoteId)) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        String imh = ".jpg";
        List<String> gf = Splitter.on(".").splitToList(imh);
        System.out.println("ok");
    }
}
