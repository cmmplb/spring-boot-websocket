package com.cmmplb.websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.dto.MessagePageQueryDTO;
import com.cmmplb.websocket.entity.MessageRecord;
import com.cmmplb.websocket.vo.ContactMessageVO;

import java.util.List;

public interface MessageRecordService extends IService<MessageRecord> {

    List<ContactMessageVO> getRecentlyMessageList();

    PageResult<ContactMessageVO> getMessageByPaged(MessagePageQueryDTO dto);
}
