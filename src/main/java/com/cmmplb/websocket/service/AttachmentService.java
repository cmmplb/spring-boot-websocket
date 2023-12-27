package com.cmmplb.websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmmplb.websocket.entity.Attachment;
import com.cmmplb.websocket.entity.User;
import com.cmmplb.websocket.vo.AttachmentVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author penglibo
 * @date 2023-12-19 13:56:02
 * @since jdk 1.8
 */
public interface AttachmentService extends IService<Attachment> {

    List<AttachmentVO> upload(MultipartFile[] files);

    void download(String fileName);
}
