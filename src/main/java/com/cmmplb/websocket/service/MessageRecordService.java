package com.cmmplb.websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.dto.MessageRecordPageQueryDTO;
import com.cmmplb.websocket.entity.MessageRecord;
import com.cmmplb.websocket.vo.MessageRecordVO;

public interface MessageRecordService extends IService<MessageRecord> {


    PageResult<MessageRecordVO> getByPaged(MessageRecordPageQueryDTO dto);
}
