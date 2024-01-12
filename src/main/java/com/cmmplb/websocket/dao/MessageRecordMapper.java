package com.cmmplb.websocket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmmplb.websocket.dto.MessageRecordPageQueryDTO;
import com.cmmplb.websocket.entity.MessageRecord;
import com.cmmplb.websocket.vo.MessageRecordVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author penglibo
 * @date 2021-04-02 00:03:34
 * MP 支持不需要 Mapper.xml
 */
public interface MessageRecordMapper extends BaseMapper<MessageRecord> {

    Page<MessageRecordVO> selectByPaged(@Param("page") Page<MessageRecordVO> page, @Param("userId") Long userId, @Param("dto") MessageRecordPageQueryDTO dto);

    Page<MessageRecordVO> selectUserMessageRecordByPaged(@Param("page") Page<MessageRecordVO> objectPage, @Param("userId") Long userId, @Param("businessId") Long businessId);

    Page<MessageRecordVO> selectGroupMessageRecordByPaged(@Param("page") Page<MessageRecordVO> objectPage, @Param("userId") Long userId, @Param("businessId") Long businessId);
}