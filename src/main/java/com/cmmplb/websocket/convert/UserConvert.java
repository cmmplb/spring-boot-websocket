package com.cmmplb.websocket.convert;

import com.cmmplb.websocket.domain.entity.User;
import com.cmmplb.websocket.domain.vo.UserInfoVO;
import org.mapstruct.Mapper;

/**
 * @author penglibo
 * @date 2022-08-03 16:56:25
 * @since jdk 1.8
 */

@Mapper
public interface UserConvert extends Converter<User, UserInfoVO> {

}
