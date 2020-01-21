package com.demai.cornel.service;

import com.demai.cornel.constant.ConfigProperties;
import com.demai.cornel.util.json.JsonUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.startup.ContextConfig;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.demai.cornel.config.BannerConfig.downloadUrl;

@Service @Slf4j public class UploadFileService {
    @Resource private ConfigProperties configProperties;
    @Resource private DownloadFileService downloadFileService;

    @PostConstruct private void initStoreFolder() {
        try {
            File f = new File(configProperties.uploadLocation);
            if (f.exists() && f.isDirectory()) {
                return;
            }
            f.setWritable(true, false);
            f.mkdirs();
            log.info("upload文件件初始化创建成功");
        } catch (Exception e) {
            log.error("upload文件件初始化创建失败！异常原因:{}", e);
        }
    }

    public String uploadFile(MultipartFile files, String key) throws IOException {
        try {
            log.info("file name is [{}]", files.getOriginalFilename());
            File saveFile = new File(configProperties.uploadLocation + key);
            String downloadUrl=null;
            if(saveFile.exists()&&saveFile.isFile()){
                log.debug("file already exist key is [{}]",key);
                 downloadUrl = downloadFileService.getDownloadUri(saveFile.getName());
                log.info("==={}", JsonUtil.toJson(downloadUrl));
                return downloadUrl;
            }
            FileUtils.copyInputStreamToFile(files.getInputStream(), saveFile);
             downloadUrl = downloadFileService.getDownloadUri(saveFile.getName());
            log.info("==={}", JsonUtil.toJson(downloadUrl));
            return downloadUrl;
        } catch (Exception e) {
            log.error("save file fail ", e);
            return null;
        } finally {
            if (files != null && files.getInputStream() != null) {
                files.getInputStream().close();
            }
        }
    }

    public static void main(String[] args) {
        String as = "常用工具.png";
        Integer last = as.lastIndexOf(".");
        System.out.println(as.substring(last + 1));
        System.out.println("name" + as.substring(as.lastIndexOf(".")));

    }
}