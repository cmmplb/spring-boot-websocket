package com.cmmplb.websocket.controller;

import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.dto.RecentlyMessagePageQueryDTO;
import com.cmmplb.websocket.result.Result;
import com.cmmplb.websocket.result.ResultUtil;
import com.cmmplb.websocket.service.RecentlyMessageService;
import com.cmmplb.websocket.vo.RecentlyMessageVO;
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
 * 最近消息管理
 */

@Slf4j
@RestController
@RequestMapping("/recently/message")
public class RecentlyMessageController {

    @Autowired
    private RecentlyMessageService recentlyMessageService;

    /**
     * 分页条件获取最近消息列表
     */
    @PostMapping(value = "/paged")
    public Result<PageResult<RecentlyMessageVO>> getByPaged(@RequestBody RecentlyMessagePageQueryDTO dto) {
        return ResultUtil.success(recentlyMessageService.getByPaged(dto));
    }

}
