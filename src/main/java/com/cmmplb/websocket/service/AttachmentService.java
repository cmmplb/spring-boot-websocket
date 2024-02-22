package com.cmmplb.websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmmplb.websocket.domain.entity.Attachment;
import com.cmmplb.websocket.domain.vo.AttachmentVO;
import org.springframework.web.multipart.MultipartFile;

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
