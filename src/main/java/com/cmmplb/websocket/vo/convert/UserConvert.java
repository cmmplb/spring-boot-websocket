package com.cmmplb.websocket.vo.convert;

import com.cmmplb.websocket.convert.Converter;
import com.cmmplb.websocket.entity.User;
import com.cmmplb.websocket.vo.UserInfoVO;
import org.mapstruct.Mapper;

/**
 * @author penglibo
 * @date 2022-08-03 16:56:25
 * @since jdk 1.8
 */

@Mapper
public interface UserConvert extends Converter<User, UserInfoVO> {

}
