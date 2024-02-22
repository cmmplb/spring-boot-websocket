package com.cmmplb.websocket.controller;

import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.beans.QueryPageBean;
import com.cmmplb.websocket.domain.vo.ContactsInfoVO;
import com.cmmplb.websocket.domain.vo.ContactsVO;
import com.cmmplb.websocket.result.Result;
import com.cmmplb.websocket.result.ResultUtil;
import com.cmmplb.websocket.service.ContactsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author penglibo
 * @date 2023-12-29 17:06:37
 * @since jdk 1.8
 * 联系人管理
 */

@Slf4j
@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactsService contactsService;

    /**
     * 分页条件获取联系人列表
     */
    @PostMapping(value = "/paged")
    public Result<PageResult<ContactsVO>> getByPaged(@RequestBody QueryPageBean bean) {
        return ResultUtil.success(contactsService.getByPaged(bean));
    }

    /**
     * 根据id获取联系人信息
     */
    @GetMapping(value = "/info/{id}")
    public Result<ContactsInfoVO> getInfoById(@PathVariable(value = "id") Long id) {
        return ResultUtil.success(contactsService.getInfoById(id));
    }

}
