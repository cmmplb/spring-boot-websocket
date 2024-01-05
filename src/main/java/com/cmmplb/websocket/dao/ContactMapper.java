package com.cmmplb.websocket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmmplb.websocket.entity.Contact;
import com.cmmplb.websocket.vo.ContactVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author penglibo
 * @date 2021-04-02 00:03:34
 * MP 支持不需要 Mapper.xml
 */
public interface ContactMapper extends BaseMapper<Contact> {

    Page<ContactVO> selectByPaged(@Param("page") Page<ContactVO> page, @Param("userId") Long userId, @Param("keywords") String keywords);
}