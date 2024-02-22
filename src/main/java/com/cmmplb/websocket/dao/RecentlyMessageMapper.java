package com.cmmplb.websocket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmmplb.websocket.domain.dto.RecentlyMessagePageQueryDTO;
import com.cmmplb.websocket.domain.entity.RecentlyMessage;
import com.cmmplb.websocket.domain.vo.RecentlyMessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author penglibo
 * @date 2024-01-05 16:56:29
 * @since jdk 1.8
 */
public interface RecentlyMessageMapper extends BaseMapper<RecentlyMessage> {

    Page<RecentlyMessageVO> selectByPaged(@Param("page") Page<RecentlyMessageVO> page, @Param("userId") Long userId, @Param("dto") RecentlyMessagePageQueryDTO dto);

    List<RecentlyMessageVO> selectByPaged2Java(@Param("userId") Long userId);
}