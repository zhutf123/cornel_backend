package com.demai.cornel.service;

import com.demai.cornel.constant.ConfigProperties;
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

@Service @Slf4j public class UploadFileService {
    @Resource private ConfigProperties configProperties;

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

    public boolean uploadFile(MultipartHttpServletRequest req) throws IOException {
        try {
            Iterator<String> a = req.getFileNames();//返回的数量与前端input数量相同, 返回的字符串即为前端input标签的name
            HashMap<String, MultipartFile> files = new HashMap<>();
            while (a.hasNext()) {
                String name = a.next();
                MultipartFile multipartFiles = req.getFile(name);//获取单个input标签上传的文件，可能为多个
                files.put(name, multipartFiles);
            }
            if (files == null || files.isEmpty()) {
                log.error("======>上传文件为空");
                return false;
            }
            for (String fileName : files.keySet()) {
                log.info("upload the file {}", fileName);
                File saveFile = new File(configProperties.uploadLocation + fileName);
                FileUtils.copyInputStreamToFile(files.get(fileName).getInputStream(), saveFile);
            }
        } catch (Exception e) {
            log.error("save file fail ", e);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String as = "常用工具.png";
        Integer last = as.lastIndexOf(".");
        System.out.println(as.substring(last + 1));
        System.out.println("name" + as.substring(as.lastIndexOf(".")));

    }
}