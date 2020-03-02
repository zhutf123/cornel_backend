package com.demai.cornel.service;

import com.demai.cornel.config.BannerConfig;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-07    22:33
 */
@Slf4j
@Service
public class BannerService {

    public List<String> getBannerDownloadUrl() {

        return BannerConfig.downloadUrl;
    }



    public List<String> purchaseGetBannerDownloadUrl() {

        return BannerConfig.purchaseBannerLsit;
    }


    public  void downloadClassResourceService(String fileName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        if (!classPathResource.exists()) {
            resp.reset();
            resp.setContentType("text/plain;charset=utf-8");
            PrintWriter writer = resp.getWriter();
            writer.write("error: file not exist! 文件不存在");
            writer.flush();
            return;
        }
        File file = new File(classPathResource.getPath());

        long length = classPathResource.getInputStream().available();
        long start = 0;
        resp.reset();
        resp.setHeader("Accept-Ranges", "byte");
        String range = req.getHeader("Range");
        String mtype = new MimetypesFileTypeMap().getContentType(fileName);
        resp.setHeader("Content-Type", mtype);
        resp.setHeader("Content-Length", new Long(length).toString());
        if (range != null) {
            resp.setHeader("Content-Range", "bytes " + new Long(start).toString() + "-" + new Long(start + length - 1).toString() + "/" + new Long(file.length()).toString());
        }
        resp.setContentType(mtype);
        resp.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes(), "utf-8"));
        long k = 0;
        int ibuffer = 65536;
        byte[] bytes = new byte[ibuffer];
        InputStream fileinputstream = classPathResource.getInputStream();
        try {
            OutputStream os = resp.getOutputStream();
            while (k < length) {
                int j = fileinputstream.read(bytes, 0, (int) (length - k < ibuffer ? length - k : ibuffer));
                if (j < 1) {
                    break;
                }
                os.write(bytes, 0, j);
                k += j;
            }
            os.flush();
            log.info("文件{},下载成功", fileName);
        } catch (Exception e) {
            log.error("文件下载异常,{}", e);
        } finally {
            fileinputstream.close();
        }
    }





}
