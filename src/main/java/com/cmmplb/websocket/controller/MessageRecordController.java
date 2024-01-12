package com.cmmplb.websocket.controller;

import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.dto.MessageRecordPageQueryDTO;
import com.cmmplb.websocket.result.Result;
import com.cmmplb.websocket.result.ResultUtil;
import com.cmmplb.websocket.service.MessageRecordService;
import com.cmmplb.websocket.vo.MessageRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2023-12-29 10:02:55
 * @since jdk 1.8
 * 消息记录管理
 */

@Slf4j
@RestController
@RequestMapping("/message/record")
public class MessageRecordController {

    @Autowired
    private MessageRecordService messageRecordService;

    /**
     * 分页条件获取用户/群消息列表
     */
    @PostMapping(value = "/paged")
    public Result<PageResult<MessageRecordVO>> getByPaged(@RequestBody MessageRecordPageQueryDTO dto) {
        return ResultUtil.success(messageRecordService.getByPaged(dto));
    }
}
