package com.demai.cornel.service;

import com.demai.cornel.constant.ConfigProperties;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.startup.ContextConfig;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

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

    public boolean uploadFile(InputStream is, String name) throws IOException {
        try {
            File saveFile = new File(configProperties.uploadLocation + name);
            log.info("upload the file {}", saveFile.toString());
            FileUtils.copyInputStreamToFile(is, saveFile);
        } catch (Exception e) {
            log.error("save file fail ", e);
            return false;
        } finally {
            if (is != null) {
                is.close();
            }
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