package com.cmmplb.websocket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmmplb.websocket.dto.MessagePageQueryDTO;
import com.cmmplb.websocket.dto.MessageRecordDTO;
import com.cmmplb.websocket.entity.MessageRecord;
import com.cmmplb.websocket.vo.ContactMessageVO;
import com.cmmplb.websocket.vo.ContactVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author penglibo
 * @date 2021-04-02 00:03:34
 * MP 支持不需要 Mapper.xml
 */
public interface MessageRecordMapper extends BaseMapper<MessageRecord> {

    List<MessageRecordDTO> selectMessageListByIds(@Param("ids") List<Long> ids);

    List<ContactMessageVO> selectContactMessageList(@Param("type") Byte type, @Param("userId") Long userId, @Param("groupIds") Set<Long> groupIds);

    Page<ContactMessageVO> selectMessageByPaged(@Param("page") Page<ContactVO> page, @Param("userId") Long userId, @Param("dto") MessagePageQueryDTO dto);
}