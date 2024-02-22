package com.cmmplb.websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmmplb.websocket.domain.entity.User;
import com.cmmplb.websocket.domain.vo.UserInfoVO;

import java.util.List;

/**
 * @author penglibo
 * @date 2023-11-17 09:44:23
 * @since jdk 1.8
 */
public interface UserService extends IService<User> {

    boolean updateAvatar(Long attachmentId);

    List<UserInfoVO> getList();

    UserInfoVO getInfo();

    UserInfoVO getInfoById(Long id);
}
