package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.dao.MessageRecordMapper;
import com.cmmplb.websocket.dto.MessagePageQueryDTO;
import com.cmmplb.websocket.dto.MessageRecordDTO;
import com.cmmplb.websocket.entity.ContactGroupUser;
import com.cmmplb.websocket.entity.MessageRecord;
import com.cmmplb.websocket.service.ContactGroupUserService;
import com.cmmplb.websocket.service.MessageRecordService;
import com.cmmplb.websocket.utils.SecurityUtil;
import com.cmmplb.websocket.vo.ContactMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageRecordServiceImpl extends ServiceImpl<MessageRecordMapper, MessageRecord> implements MessageRecordService {

    @Autowired
    private ContactGroupUserService contactGroupUserService;

    @Override
    public List<ContactMessageVO> getRecentlyMessageList() {
        Long userId = SecurityUtil.getUserId();
        // 获取用户消息
        List<ContactMessageVO> list = baseMapper.selectContactMessageList((byte) 1, userId, null);

        // 获取群消息
        Set<Long> groupIds = new HashSet<>();
        // 加入的群
        List<ContactGroupUser> contactGroupUsers = contactGroupUserService.list(new LambdaQueryWrapper<ContactGroupUser>().eq(ContactGroupUser::getUserId, userId));
        if (!CollectionUtils.isEmpty(contactGroupUsers)) {
            for (ContactGroupUser contactGroupUser : contactGroupUsers) {
                groupIds.add(contactGroupUser.getContactGroupId());
            }
        }
        // 获取群消息
        if (!CollectionUtils.isEmpty(groupIds)) {
            List<ContactMessageVO> userMessageList = baseMapper.selectContactMessageList((byte) 2, null, groupIds);
            list.addAll(userMessageList);
        }

        // 根据id获取消息和创建时间
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> ids = list.stream().map(ContactMessageVO::getId).collect(Collectors.toList());
            List<MessageRecordDTO> userMessageList = baseMapper.selectMessageListByIds(ids);
            for (ContactMessageVO contactMessageVO : list) {
                for (MessageRecordDTO messageRecordDTO : userMessageList) {
                    if (contactMessageVO.getId().equals(messageRecordDTO.getId())) {
                        contactMessageVO.setMessage(messageRecordDTO.getMessage());
                        contactMessageVO.setCreateTime(messageRecordDTO.getCreateTime());
                        break;
                    }
                }
            }
        }
        // 按时间倒序
        return list.stream().sorted(Comparator.comparing(ContactMessageVO::getCreateTime/*, Comparator.reverseOrder()*/))
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<ContactMessageVO> getMessageByPaged(MessagePageQueryDTO dto) {
        Long userId = SecurityUtil.getUserId();
        Page<ContactMessageVO> page = baseMapper.selectMessageByPaged(new Page<>(dto.getCurrent(), dto.getSize()), userId, dto);
        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}
