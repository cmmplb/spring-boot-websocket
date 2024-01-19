package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.beans.QueryPageBean;
import com.cmmplb.websocket.dao.ContactsMapper;
import com.cmmplb.websocket.entity.Contacts;
import com.cmmplb.websocket.service.ContactsService;
import com.cmmplb.websocket.utils.SecurityUtil;
import com.cmmplb.websocket.vo.ContactsVO;
import org.springframework.stereotype.Service;

/**
 * @author penglibo
 * @date 2023-12-19 13:56:02
 * @since jdk 1.8
 */
@Service
public class ContactsServiceImpl extends ServiceImpl<ContactsMapper, Contacts> implements ContactsService {

    @Override
    public PageResult<ContactsVO> getByPaged(QueryPageBean bean) {
        Long userId = SecurityUtil.getUserId();
        Page<ContactsVO> page = baseMapper.selectByPaged(new Page<>(bean.getCurrent(), bean.getSize()), userId, bean.getKeywords());
        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}
