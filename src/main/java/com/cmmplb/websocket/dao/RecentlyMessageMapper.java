package com.cmmplb.websocket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmmplb.websocket.dto.RecentlyMessageDTO;
import com.cmmplb.websocket.dto.RecentlyMessagePageQueryDTO;
import com.cmmplb.websocket.entity.RecentlyMessage;
import com.cmmplb.websocket.vo.RecentlyMessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author penglibo
 * @date 2024-01-05 16:56:29
 * @since jdk 1.8
 */
public interface RecentlyMessageMapper extends BaseMapper<RecentlyMessage> {

    Page<RecentlyMessageVO> selectByPaged(@Param("page") Page<RecentlyMessageVO> page, @Param("userId") Long userId, @Param("dto") RecentlyMessagePageQueryDTO dto);

    List<RecentlyMessageDTO> selectListByBusinessIds(@Param("sendBusinessIds") List<Long> sendBusinessIds);
}