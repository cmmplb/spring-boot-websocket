package com.cmmplb.websocket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmmplb.websocket.domain.entity.Contacts;
import com.cmmplb.websocket.domain.vo.ContactsInfoVO;
import com.cmmplb.websocket.domain.vo.ContactsVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author penglibo
 * @date 2021-04-02 00:03:34
 * MP 支持不需要 Mapper.xml
 */
public interface ContactsMapper extends BaseMapper<Contacts> {

    Page<ContactsVO> selectByPaged(@Param("page") Page<ContactsVO> page, @Param("userId") Long userId, @Param("keywords") String keywords);

    ContactsInfoVO selectInfoById(@Param("id") Long id);
}