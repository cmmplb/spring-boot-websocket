package com.cmmplb.websocket.convert;

import com.cmmplb.websocket.entity.Attachment;
import com.cmmplb.websocket.vo.AttachmentVO;
import org.mapstruct.Mapper;

/**
 * @author penglibo
 * @date 2022-08-03 16:56:25
 * @since jdk 1.8
 */

@Mapper
public interface AttachmentConvert extends Converter<Attachment, AttachmentVO> {

}
