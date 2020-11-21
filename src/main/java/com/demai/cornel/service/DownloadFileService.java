package com.demai.cornel.service;

import com.demai.cornel.constant.ConfigProperties;
import com.demai.cornel.util.HttpUtils;
import org.apache.catalina.startup.ContextConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Service
public class DownloadFileService {
    @Resource
    private ConfigProperties configProperties;

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadFileService.class);

    private static final  String DOWNLOAD_PATH_FORMAT =  "/file/download?key=%s";

    public  void downloadService(String fileName, HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //ClassPathResource classPathResource = new ClassPathResource(fileName);
        String location = configProperties.uploadLocation+fileName;
        File file = new File(location);
        if (!file.exists()|| !file.isFile()) {
            resp.reset();
            resp.setContentType("text/plain;charset=utf-8");
            PrintWriter writer = resp.getWriter();
            LOGGER.error("file no exist path |{}|",fileName);
            writer.write("error: file not exist! 文件不存在");
            writer.flush();
            return;
        }
        InputStream si = new FileInputStream(file);

        long length = si.available();
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
        InputStream fileinputstream = si;
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
            LOGGER.info("文件{},下载成功", fileName);
        } catch (Exception e) {
            LOGGER.error("文件下载异常,{}", e);
        } finally {
            fileinputstream.close();
        }
    }

    public  String getDownloadUri(String key) {
        return String.format(StringUtils.join(configProperties.downloadHost,DOWNLOAD_PATH_FORMAT),
                HttpUtils.urlEncode(key));
    }

}
