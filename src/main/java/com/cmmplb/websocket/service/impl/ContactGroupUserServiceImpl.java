package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.dao.ContactGroupUserMapper;
import com.cmmplb.websocket.entity.ContactGroupUser;
import com.cmmplb.websocket.service.ContactGroupUserService;
import org.springframework.stereotype.Service;

@Service
public class ContactGroupUserServiceImpl extends ServiceImpl<ContactGroupUserMapper, ContactGroupUser> implements ContactGroupUserService{


}
