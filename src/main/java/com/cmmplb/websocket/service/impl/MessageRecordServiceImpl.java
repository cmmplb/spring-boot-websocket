package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.constants.GlobalConstants;
import com.cmmplb.websocket.dao.MessageRecordMapper;
import com.cmmplb.websocket.domain.dto.MessageRecordPageQueryDTO;
import com.cmmplb.websocket.domain.entity.MessageRecord;
import com.cmmplb.websocket.domain.vo.MessageRecordVO;
import com.cmmplb.websocket.handler.ChatTextWebSocketHandler;
import com.cmmplb.websocket.service.MessageRecordService;
import com.cmmplb.websocket.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author penglibo
 * @date 2023-12-19 13:56:02
 * @since jdk 1.8
 */
@Service
@Transactional
public class MessageRecordServiceImpl extends ServiceImpl<MessageRecordMapper, MessageRecord> implements MessageRecordService {

    @Override
    public PageResult<MessageRecordVO> getByPaged(MessageRecordPageQueryDTO dto) {
        Long userId = SecurityUtil.getUserId();
        Page<MessageRecordVO> page;
        String[] split = dto.getBusinessId().split("-");
        byte type = Byte.parseByte(split[0]);
        Long businessId = Long.parseLong(split[1]);
        if (type == GlobalConstants.NUM_ONE) {
            // 获取用户消息
            page = baseMapper.selectUserMessageRecordByPaged(new Page<>(dto.getCurrent(), dto.getSize()), userId, businessId);

        } else {
            // 获取群消息
            page = baseMapper.selectGroupMessageRecordByPaged(new Page<>(dto.getCurrent(), dto.getSize()), userId, businessId);
        }
        // 标记消息数量已读
        baseMapper.read(userId, type, businessId);
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    @Override
    public boolean read(String uuid) {
        // 标记消息数量已读
        MessageRecord messageRecord = baseMapper.selectOne(new LambdaQueryWrapper<MessageRecord>().eq(MessageRecord::getUuid, uuid));
        if (null != messageRecord) {
            MessageRecord messageRecordUp = new MessageRecord();
            messageRecordUp.setId(messageRecord.getId());
            messageRecordUp.setStatus(GlobalConstants.NUM_ONE);
            messageRecordUp.setUpdateTime(new Date());
            messageRecordUp.setUpdateBy(SecurityUtil.getUserId());
            baseMapper.updateById(messageRecordUp);

            // 通知客户端消息已读
            ChatTextWebSocketHandler.read(messageRecord.getSendBusinessId(), messageRecord.getType(), messageRecord.getReceiveBusinessId(), uuid);
        }
        return true;
    }

    @Override
    public Boolean readBusiness(String businessId, Long userId) {
        String[] split = businessId.split("-");
        // 标记消息数量已读
        List<MessageRecord> list = baseMapper.selectList(new LambdaQueryWrapper<MessageRecord>()
                .eq(MessageRecord::getType, Byte.parseByte(split[0]))
                .eq(MessageRecord::getSendBusinessId, Long.parseLong(split[1]))
                .eq(MessageRecord::getStatus, GlobalConstants.NUM_ZERO)
        );
        if (!CollectionUtils.isEmpty(list)) {
            List<MessageRecord> listUp = new ArrayList<>();
            for (MessageRecord messageRecord : list) {
                MessageRecord messageRecordUp = new MessageRecord();
                messageRecordUp.setId(messageRecord.getId());
                messageRecordUp.setStatus(GlobalConstants.NUM_ONE);
                messageRecordUp.setUpdateTime(new Date());
                messageRecordUp.setUpdateBy(SecurityUtil.getUserId());
                listUp.add(messageRecordUp);
            }
            this.updateBatchById(listUp);
            // 通知客户端消息已读
            ChatTextWebSocketHandler.readBusiness(Long.parseLong(split[1]), 1 + "-" + userId);
        }
        return true;
    }
}
