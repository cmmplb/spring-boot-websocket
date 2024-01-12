package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.dao.MessageRecordMapper;
import com.cmmplb.websocket.dto.MessageRecordPageQueryDTO;
import com.cmmplb.websocket.entity.MessageRecord;
import com.cmmplb.websocket.service.MessageRecordService;
import com.cmmplb.websocket.utils.SecurityUtil;
import com.cmmplb.websocket.vo.MessageRecordVO;
import org.springframework.stereotype.Service;

/**
 * @author penglibo
 * @date 2023-12-19 13:56:02
 * @since jdk 1.8
 */
@Service
public class MessageRecordServiceImpl extends ServiceImpl<MessageRecordMapper, MessageRecord> implements MessageRecordService {


    @Override
    public PageResult<MessageRecordVO> getByPaged(MessageRecordPageQueryDTO dto) {
        Long userId = SecurityUtil.getUserId();
        Page<MessageRecordVO> page;
        if (dto.getType().equals((byte) 1)) {
            // 获取用户消息
            page = baseMapper.selectUserMessageRecordByPaged(new Page<>(dto.getCurrent(), dto.getSize()), userId, dto.getBusinessId());

        } else {
            // 获取群消息
            page = baseMapper.selectGroupMessageRecordByPaged(new Page<>(dto.getCurrent(), dto.getSize()), userId, dto.getBusinessId());
        }
        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}
