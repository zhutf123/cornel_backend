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


    public List<String> uploadFile(MultipartFile files) throws IOException {
        try {
            File saveFile = new File(configProperties.uploadLocation + UUID.randomUUID() + files.getOriginalFilename()
                    .substring(files.getOriginalFilename().lastIndexOf(".")));
            FileUtils.copyInputStreamToFile(files.getInputStream(), saveFile);
            downloadUrl.add(downloadFileService.getDownloadUri(saveFile.getName()));
            log.info("==={}", JsonUtil.toJson(downloadUrl));
//            Iterator<String> a = req.getFileNames();//返回的数量与前端input数量相同, 返回的字符串即为前端input标签的name
//            HashMap<String, MultipartFile> files = new HashMap<>();
//            List<String> downloadUrl = new ArrayList<>();
//            while (a.hasNext()) {
//                String name = a.next();
//                MultipartFile multipartFiles = req.getFile(name);//获取单个input标签上传的文件，可能为多个
//                files.put(name, multipartFiles);
//            }
//            if (files == null || files.isEmpty()) {
//                log.error("======>上传文件为空");
//                return null;
//            }
//            for (String fileName : files.keySet()) {
//                log.info("upload the file {}", fileName);
//                File saveFile = new File(configProperties.uploadLocation + fileName);
//                FileUtils.copyInputStreamToFile(files.get(fileName).getInputStream(), saveFile);
//                downloadUrl.add(downloadFileService.getDownloadUri(fileName));
//            }
        } catch (Exception e) {
            log.error("save file fail ", e);
            return null;
        }
        return downloadUrl;
    }

    public static void main(String[] args) {
        String as = "常用工具.png";
        Integer last = as.lastIndexOf(".");
        System.out.println(as.substring(last + 1));
        System.out.println("name" + as.substring(as.lastIndexOf(".")));

    }
}