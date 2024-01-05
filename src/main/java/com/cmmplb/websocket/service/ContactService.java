package com.cmmplb.websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.beans.QueryPageBean;
import com.cmmplb.websocket.entity.Contact;
import com.cmmplb.websocket.vo.ContactVO;

public interface ContactService extends IService<Contact> {

    PageResult<ContactVO> getByPaged(QueryPageBean bean);
}
