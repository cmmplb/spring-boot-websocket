package com.cmmplb.websocket.controller;

import com.cmmplb.websocket.result.Result;
import com.cmmplb.websocket.result.ResultUtil;
import com.cmmplb.websocket.service.AttachmentService;
import com.cmmplb.websocket.domain.vo.AttachmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author penglibo
 * @date 2023-07-28 09:09:29
 * @since jdk 1.8
 * 附件管理
 */

@Slf4j
@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 附件上传
     * @param files 文件数组
     * @return 上传文件
     */
    @PostMapping("/upload")
    Result<List<AttachmentVO>> upload(@RequestPart("files") MultipartFile[] files) {
        return ResultUtil.success(attachmentService.upload(files));
    }

    /**
     * 文件下载
     * @param fileName 文件名称
     */
    @GetMapping(value = "/download/{fileName:.+}")
    void download(@PathVariable(value = "fileName") String fileName) {
        attachmentService.download(fileName);
    }
}
