package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.beans.QueryPageBean;
import com.cmmplb.websocket.dao.ContactMapper;
import com.cmmplb.websocket.entity.Contact;
import com.cmmplb.websocket.service.ContactService;
import com.cmmplb.websocket.utils.SecurityUtil;
import com.cmmplb.websocket.vo.ContactVO;
import org.springframework.stereotype.Service;

/**
 * @author penglibo
 * @date 2023-12-19 13:56:02
 * @since jdk 1.8
 */
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements ContactService {

    @Override
    public PageResult<ContactVO> getByPaged(QueryPageBean bean) {
        Long userId = SecurityUtil.getUserId();
        Page<ContactVO> page = baseMapper.selectByPaged(new Page<>(bean.getCurrent(), bean.getSize()), userId, bean.getKeywords());
        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}
