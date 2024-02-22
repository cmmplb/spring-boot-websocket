package com.cmmplb.websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmmplb.websocket.beans.PageResult;
import com.cmmplb.websocket.domain.dto.RecentlyMessagePageQueryDTO;
import com.cmmplb.websocket.domain.entity.RecentlyMessage;
import com.cmmplb.websocket.domain.vo.RecentlyMessageVO;

/**
* @author penglibo
* @date 2024-01-05 16:55:53
* @since jdk 1.8
*/
public interface RecentlyMessageService extends IService<RecentlyMessage> {

    PageResult<RecentlyMessageVO> getByPaged(RecentlyMessagePageQueryDTO dto);

}
