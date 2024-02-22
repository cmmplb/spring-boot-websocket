package com.cmmplb.websocket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmmplb.websocket.domain.entity.MessageRecord;
import com.cmmplb.websocket.domain.vo.MessageRecordVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author penglibo
 * @date 2021-04-02 00:03:34
 * MP 支持不需要 Mapper.xml
 */
public interface MessageRecordMapper extends BaseMapper<MessageRecord> {

    Page<MessageRecordVO> selectUserMessageRecordByPaged(@Param("page") Page<MessageRecordVO> objectPage, @Param("userId") Long userId, @Param("businessId") Long businessId);

    Page<MessageRecordVO> selectGroupMessageRecordByPaged(@Param("page") Page<MessageRecordVO> objectPage, @Param("userId") Long userId, @Param("businessId") Long businessId);

    void read(@Param("userId") Long userId, @Param("type") byte type, @Param("businessId") Long businessId);
}