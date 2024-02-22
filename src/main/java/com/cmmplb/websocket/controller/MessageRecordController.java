package com.cmmplb.websocket.controller;

import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.domain.dto.MessageRecordPageQueryDTO;
import com.cmmplb.websocket.domain.vo.MessageRecordVO;
import com.cmmplb.websocket.result.Result;
import com.cmmplb.websocket.result.ResultUtil;
import com.cmmplb.websocket.service.MessageRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 标记单条消息已读
     */
    @PostMapping(value = "/read/{uuid}")
    public Result<Boolean> read(@PathVariable(value = "uuid") String uuid) {
        return ResultUtil.success(messageRecordService.read(uuid));
    }

    /**
     * 标记用户/群消息已读
     */
    @PostMapping(value = "/read/business/{businessId}/{userId}")
    public Result<Boolean> readBusiness(@PathVariable(value = "businessId") String businessId, @PathVariable(value = "userId") Long userId) {
        return ResultUtil.success(messageRecordService.readBusiness(businessId, userId));
    }
}
