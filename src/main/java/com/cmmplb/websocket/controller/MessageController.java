package com.cmmplb.websocket.controller;

import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.dto.MessagePageQueryDTO;
import com.cmmplb.websocket.result.Result;
import com.cmmplb.websocket.result.ResultUtil;
import com.cmmplb.websocket.service.MessageRecordService;
import com.cmmplb.websocket.vo.ContactMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author penglibo
 * @date 2023-12-29 10:02:55
 * @since jdk 1.8
 * 消息管理
 */

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageRecordService messageRecordService;

    /**
     * 获取最近消息列表
     */
    @GetMapping(value = "/recently/list")
    public Result<List<ContactMessageVO>> getRecentlyMessageList() {
        return ResultUtil.success(messageRecordService.getRecentlyMessageList());
    }

    /**
     * 分页条件获取用户/群消息列表
     */
    @PostMapping(value = "/paged")
    public Result<PageResult<ContactMessageVO>> getMessageByPaged(@RequestBody MessagePageQueryDTO dto) {
        return ResultUtil.success(messageRecordService.getMessageByPaged(dto));
    }
}
