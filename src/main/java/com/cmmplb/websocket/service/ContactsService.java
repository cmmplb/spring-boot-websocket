package com.cmmplb.websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.beans.QueryPageBean;
import com.cmmplb.websocket.entity.Contacts;
import com.cmmplb.websocket.vo.ContactsVO;

public interface ContactsService extends IService<Contacts> {

    PageResult<ContactsVO> getByPaged(QueryPageBean bean);
}
