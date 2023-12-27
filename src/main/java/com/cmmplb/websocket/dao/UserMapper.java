package com.cmmplb.websocket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmmplb.websocket.entity.User;
import com.cmmplb.websocket.vo.UserInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author penglibo
 * @date 2021-04-02 00:03:34
 * MP 支持不需要 Mapper.xml
 */

public interface UserMapper extends BaseMapper<User> {

    UserInfoVO selectInfoById(@Param("id") Long id);

    List<UserInfoVO> selectList();
}
