package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.dao.AttachmentDataMapper;
import com.cmmplb.websocket.domain.entity.AttachmentData;
import com.cmmplb.websocket.service.AttachmentDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author penglibo
 * @date 2023-12-19 13:56:02
 * @since jdk 1.8
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttchmentDataServiceImpl extends ServiceImpl<AttachmentDataMapper, AttachmentData> implements AttachmentDataService {

}
