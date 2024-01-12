package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.dao.RecentlyMessageMapper;
import com.cmmplb.websocket.dto.RecentlyMessageDTO;
import com.cmmplb.websocket.dto.RecentlyMessagePageQueryDTO;
import com.cmmplb.websocket.entity.RecentlyMessage;
import com.cmmplb.websocket.service.RecentlyMessageService;
import com.cmmplb.websocket.utils.SecurityUtil;
import com.cmmplb.websocket.vo.RecentlyMessageVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author penglibo
 * @date 2024-01-05 16:55:53
 * @since jdk 1.8
 */
@Service
public class RecentlyMessageServiceImpl extends ServiceImpl<RecentlyMessageMapper, RecentlyMessage> implements RecentlyMessageService {

    @Override
    public PageResult<RecentlyMessageVO> getByPaged(RecentlyMessagePageQueryDTO dto) {
        Long userId = SecurityUtil.getUserId();
        Page<RecentlyMessageVO> page = baseMapper.selectByPaged(new Page<>(dto.getCurrent(), dto.getSize()), userId, dto);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<Long> businessIds = page.getRecords().stream().map(RecentlyMessageVO::getBusinessId).collect(Collectors.toList());
            List<RecentlyMessageDTO> recentlyMessageList = baseMapper.selectListByBusinessIds(businessIds);
            // 获取最新消息和发送时间
            for (RecentlyMessageVO vo : page.getRecords()) {
                for (RecentlyMessageDTO recentlyMessageDTO : recentlyMessageList) {
                    if (vo.getBusinessId().equals(recentlyMessageDTO.getReceiveBusinessId())) {
                        vo.setMessage(vo.getSendTime().compareTo(recentlyMessageDTO.getSendTime()) < 0 ? recentlyMessageDTO.getMessage() : vo.getMessage());
                        vo.setSendTime(vo.getSendTime().compareTo(recentlyMessageDTO.getSendTime()) < 0 ? recentlyMessageDTO.getSendTime() : vo.getSendTime());
                        break;
                    }
                }
            }
        }
        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}
