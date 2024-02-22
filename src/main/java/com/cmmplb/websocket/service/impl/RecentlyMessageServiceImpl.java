package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.constants.GlobalConstants;
import com.cmmplb.websocket.dao.RecentlyMessageMapper;
import com.cmmplb.websocket.domain.dto.RecentlyMessagePageQueryDTO;
import com.cmmplb.websocket.domain.entity.MessageRecord;
import com.cmmplb.websocket.domain.entity.RecentlyMessage;
import com.cmmplb.websocket.domain.vo.RecentlyMessageVO;
import com.cmmplb.websocket.service.MessageRecordService;
import com.cmmplb.websocket.service.RecentlyMessageService;
import com.cmmplb.websocket.utils.ListUtil;
import com.cmmplb.websocket.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author penglibo
 * @date 2024-01-05 16:55:53
 * @since jdk 1.8
 */
@Service
public class RecentlyMessageServiceImpl extends ServiceImpl<RecentlyMessageMapper, RecentlyMessage> implements RecentlyMessageService {

    @Autowired
    private MessageRecordService messageRecordService;

    @Override
    public PageResult<RecentlyMessageVO> getByPaged(RecentlyMessagePageQueryDTO dto) {
        Long userId = SecurityUtil.getUserId();
        // 由于使用group by分组后，分组的数据不是最新的，有几种方案：
        // 1.先查询列表后排序，再分组
        // 2.使用group by后配合使用any_value(max(column))，这里不使用这个因为id只有发送人id和接收id，根据时间取最新
        // return getPage(dto, userId);
        // 3.java分组，排序，手动分页
        return getPage2Java(dto, userId);
    }

    private PageResult<RecentlyMessageVO> getPage(RecentlyMessagePageQueryDTO dto, Long userId) {
        Page<RecentlyMessageVO> page = baseMapper.selectByPaged(new Page<>(dto.getCurrent(), dto.getSize()), userId, dto);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<Date> sendTimeList = page.getRecords().stream().map(RecentlyMessageVO::getSendTime).collect(Collectors.toList());
            List<RecentlyMessage> recentlyMessageList = baseMapper.selectList(new LambdaQueryWrapper<RecentlyMessage>()
                    .and(query -> query.eq(RecentlyMessage::getSendBusinessId, userId).or().eq(RecentlyMessage::getReceiveBusinessId, userId))
                    .in(RecentlyMessage::getSendTime, sendTimeList)
            );
            // 获取最新消息
            for (RecentlyMessageVO vo : page.getRecords()) {
                for (RecentlyMessage recentlyMessage : recentlyMessageList) {
                    if (vo.getSendTime().equals(recentlyMessage.getSendTime())) {
                        vo.setMessage(recentlyMessage.getMessage());
                        vo.setUuid(recentlyMessage.getUuid());
                        break;
                    }
                }
            }
        }
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    public PageResult<RecentlyMessageVO> getPage2Java(RecentlyMessagePageQueryDTO dto, Long userId) {
        // 查出关联数据分组排序后分页
        List<RecentlyMessageVO> list = baseMapper.selectByPaged2Java(userId);
        if (!CollectionUtils.isEmpty(list)) {
            Set<String> businessIds = list.stream().map(ele -> ele.getBusinessId().replace(ele.getType() + "-", ""))
                    .collect(Collectors.toSet());
            List<MessageRecord> messageRecordList = messageRecordService.list(new LambdaQueryWrapper<MessageRecord>()
                    .eq(MessageRecord::getStatus, GlobalConstants.NUM_ZERO)
                    .in(MessageRecord::getSendBusinessId, businessIds)
            );
            Map<Long, List<MessageRecord>> messageRecordMap = messageRecordList.stream().collect(Collectors.groupingBy(MessageRecord::getSendBusinessId));
            // 分组
            Map<String, List<RecentlyMessageVO>> businessIdMap = list.stream().collect(Collectors.groupingBy(RecentlyMessageVO::getBusinessId));
            List<RecentlyMessageVO> res = new ArrayList<>();
            for (Map.Entry<String, List<RecentlyMessageVO>> entry : businessIdMap.entrySet()) {
                List<RecentlyMessageVO> value = entry.getValue();
                for (RecentlyMessageVO vo : value) {
                    for (MessageRecord messageRecord : messageRecordList) {
                        if (vo.getBusinessId().equals(messageRecord.getType() + "-" + messageRecord.getSendBusinessId())) {
                            vo.setCount(messageRecordMap.get(messageRecord.getSendBusinessId()).size());
                            break;
                        }
                    }
                }
                // 获取最新时间
                value.stream().max(Comparator.comparing(RecentlyMessageVO::getSendTime)).ifPresent(res::add);
            }
            res = res.stream().sorted(Comparator.comparing(RecentlyMessageVO::getSendTime).reversed()).collect(Collectors.toList());
            return new PageResult<>((long) res.size(), ListUtil.startPage(res, dto.getCurrent(), dto.getSize()));
        }
        return new PageResult<>();
    }
}
