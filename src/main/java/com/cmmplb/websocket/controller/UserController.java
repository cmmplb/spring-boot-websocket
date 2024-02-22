package com.cmmplb.websocket.controller;

import com.cmmplb.websocket.domain.vo.UserInfoVO;
import com.cmmplb.websocket.result.Result;
import com.cmmplb.websocket.result.ResultUtil;
import com.cmmplb.websocket.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author penglibo
 * @date 2023-12-19 11:49:30
 * @since jdk 1.8
 * 用户管理
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 修改头像
     */
    @PutMapping(value = "/avatar/{attachmentId}")
    public Result<Boolean> updateAvatar(@PathVariable(value = "attachmentId") Long attachmentId) {
        return ResultUtil.success(userService.updateAvatar(attachmentId));
    }

    /**
     * 获取用户信息
     */
    @GetMapping(value = "/info")
    public Result<UserInfoVO> getInfo() {
        return ResultUtil.success(userService.getInfo());
    }

    /**
     * 获取用户信息
     */
    @GetMapping(value = "/info/{id}")
    public Result<UserInfoVO> getInfoById(@PathVariable(value = "id") Long id) {
        return ResultUtil.success(userService.getInfoById(id));
    }

    /**
     * 获取用户列表
     */
    @GetMapping(value = "/list")
    public Result<List<UserInfoVO>> getList() {
        return ResultUtil.success(userService.getList());
    }

    /**
     * 获取离线用户列表
     */
    @GetMapping(value = "/offline/list")
    public Result<List<UserInfoVO>> getOfflineUserList() {
        return ResultUtil.success(userService.getList());
    }
}
