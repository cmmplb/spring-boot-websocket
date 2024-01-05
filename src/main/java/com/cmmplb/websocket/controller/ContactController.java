package com.cmmplb.websocket.controller;

import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.beans.QueryPageBean;
import com.cmmplb.websocket.result.Result;
import com.cmmplb.websocket.result.ResultUtil;
import com.cmmplb.websocket.service.ContactService;
import com.cmmplb.websocket.vo.ContactVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2023-12-29 17:06:37
 * @since jdk 1.8
 * 通讯录管理
 */

@Slf4j
@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    /**
     * 分页条件获取联系人列表
     */
    @PostMapping(value = "/paged")
    public Result<PageResult<ContactVO>> getByPaged(@RequestBody QueryPageBean bean) {
        return ResultUtil.success(contactService.getByPaged(bean));
    }


}
