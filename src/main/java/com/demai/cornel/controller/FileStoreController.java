package com.demai.cornel.controller;

import com.demai.cornel.service.DownloadFileService;
import com.demai.cornel.service.UploadFileService;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.json.JsonResultUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.uploadfile.UploadResp;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-17    16:19
 */
@RestController @RequestMapping("/file/") @Slf4j public class FileStoreController {

    @Resource private UploadFileService uploadFileService;
    @Resource private DownloadFileService downloadFileService;

    @PostMapping(value = "/upload", headers = ("content-type=multipart/*"))
    @ResponseBody
    public JsonResult upload(@RequestParam("file") MultipartFile req, HttpServletResponse resp,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "name", required = true) String name) {
        log.info("upload the file the file name is:{}", name);
        if (Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(name)) {
            log.error("upload file fail lack the name ");
            JsonResult.success(UploadResp.builder().optResult(UploadResp.CODE_ENUE.PARAM_ERROR.getValue()).build());
        }
        List<String> downloadUrl = new ArrayList<>();
        try {
            downloadUrl = uploadFileService.uploadFile(req);
            if (downloadUrl==null) {
                JsonResult
                        .success(UploadResp.builder().optResult(UploadResp.CODE_ENUE.SERVER_ERROR.getValue()).build());
            }
        } catch (IOException e) {
            log.error("upload file fail due to ", e);
            JsonResult.success(UploadResp.builder().optResult(UploadResp.CODE_ENUE.SERVER_ERROR.getValue()).build());
        }
        return JsonResult.success(
                UploadResp.builder().optResult(UploadResp.CODE_ENUE.SERVER_ERROR.getValue()).url(downloadUrl).build());
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET) @ResponseBody public void download(
            @RequestParam(value = "key") String key, @RequestParam(value = "name", required = false) String name,
            HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("download the file the key={},name={}", key, name);
        String fileName = key;
        if (fileName == null || fileName.trim().length() == 0) {
            resp.reset();
            resp.setContentType("text/plain;charset=utf-8");
            PrintWriter writer = resp.getWriter();
            writer.write("error:can't get the file name! 不能获取文件名");
            writer.flush();
            return;
        }
        downloadFileService.downloadService(fileName, req, resp);
    }
}
