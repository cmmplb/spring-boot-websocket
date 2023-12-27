package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.dao.UserMapper;
import com.cmmplb.websocket.entity.User;
import com.cmmplb.websocket.exception.BusinessException;
import com.cmmplb.websocket.service.UserService;
import com.cmmplb.websocket.utils.SecurityUtil;
import com.cmmplb.websocket.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author penglibo
 * @date 2023-11-17 09:44:28
 * @since jdk 1.8
 */

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public List<UserInfoVO> getList() {
        return baseMapper.selectList();
    }

    @Override
    public UserInfoVO getInfo() {
        Long userId = SecurityUtil.getUserId();
        return baseMapper.selectInfoById(userId);
    }

    @Override
    public boolean updateAvatar(Long attachmentId) {
        Long userId = SecurityUtil.getUserId();
        User user = baseMapper.selectById(userId);
        if (null == user) {
            throw new BusinessException("用户信息不存在");
        }
        User userDb = new User();
        userDb.setId(userId);
        userDb.setAvatar(attachmentId);
        return baseMapper.updateById(userDb) > 0;
    }
}