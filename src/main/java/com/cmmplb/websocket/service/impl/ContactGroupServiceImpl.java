package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.dao.ContactGroupMapper;
import com.cmmplb.websocket.entity.ContactGroup;
import com.cmmplb.websocket.service.ContactGroupService;
import org.springframework.stereotype.Service;
@Service
public class ContactGroupServiceImpl extends ServiceImpl<ContactGroupMapper, ContactGroup> implements ContactGroupService {

}
