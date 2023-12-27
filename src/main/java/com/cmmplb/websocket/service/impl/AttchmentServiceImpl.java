package com.cmmplb.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.websocket.convert.AttachmentConvert;
import com.cmmplb.websocket.dao.AttachmentMapper;
import com.cmmplb.websocket.entity.Attachment;
import com.cmmplb.websocket.entity.AttachmentData;
import com.cmmplb.websocket.exception.BusinessException;
import com.cmmplb.websocket.service.AttachmentDataService;
import com.cmmplb.websocket.service.AttachmentService;
import com.cmmplb.websocket.utils.ConverterUtil;
import com.cmmplb.websocket.utils.SecurityUtil;
import com.cmmplb.websocket.vo.AttachmentVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author penglibo
 * @date 2023-12-19 13:56:02
 * @since jdk 1.8
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttchmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements AttachmentService {

    @Autowired
    private AttachmentDataService attachmentDataService;

    @Value("${attchment.download}")
    private String download;

    @Override
    public List<AttachmentVO> upload(MultipartFile[] files) {
        if (null == files || files.length == 0) {
            throw new BusinessException("请选择文件");
        }
        List<AttachmentVO> list = new ArrayList<>();
        Attachment attachment = null;
        for (MultipartFile file : files) {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            String suffixName = "";
            if (StringUtils.isEmpty(fileName) && fileName.lastIndexOf(".") != -1) {
                // 根据文件名获取文件后缀名
                suffixName = fileName.substring(fileName.lastIndexOf("."));
            }
            try {
                // 生成MD5后的文件名,防止文件名称重复文件问题
                String md5 = DigestUtils.md5Hex(DigestUtils.md5Hex(file.getBytes()) + fileName + suffixName);
                fileName = md5 + suffixName;

                // 查询资源是否存在
                int count = attachmentDataService.count(new LambdaQueryWrapper<AttachmentData>().eq(AttachmentData::getMd5, md5));
                if (count == 0) {
                    // 保存资源信息
                    AttachmentData attachmentData = new AttachmentData();
                    attachmentData.setMd5(md5);
                    attachmentData.setData(file.getBytes());
                    attachmentData.setCreateTime(new Date());
                    attachmentDataService.save(attachmentData);
                }

                attachment = new Attachment();
                attachment.setName(file.getOriginalFilename());
                attachment.setType(suffixName);
                attachment.setSize(file.getSize());
                attachment.setMd5(md5);
                // 添加当前文件访问路径
                attachment.setUrl(download + fileName);
                attachment.setCreateTime(new Date());
                baseMapper.insert(attachment);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new BusinessException("图片上传失败");
            }
            list.add(ConverterUtil.convert(AttachmentConvert.class, attachment));
        }
        return list;
    }

    @Override
    public void download(String fileName) {
        OutputStream os = null;
        try {
            String md5;
            if (fileName.lastIndexOf(".") != -1) {
                // 根据文件名获取文件前缀名，前缀是md5加密后的文件名
                md5 = fileName.substring(0, fileName.indexOf("."));
            } else {
                md5 = fileName;
            }
            Attachment attachment = baseMapper.selectOne(new LambdaQueryWrapper<Attachment>().eq(Attachment::getMd5, md5).last("limit 1"));
            if (attachment == null) {
                throw new BusinessException("文件不存在！");
            }
            HttpServletResponse response = SecurityUtil.getResponse();
            response.setContentType("application/force-download");
            // 填充文件真实名称
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" +
                    new String(URLEncoder.encode(attachment.getName(), "utf-8").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            byte[] buff = new byte[1024];

            // 获取关联的资源信息
            AttachmentData attachmentData = attachmentDataService.getOne(new LambdaQueryWrapper<AttachmentData>().eq(AttachmentData::getMd5, md5));
            InputStream in = new ByteArrayInputStream(attachmentData.getData());
            os = response.getOutputStream();
            int len;
            while ((len = in.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
        } catch (Exception e) {
            log.error("下载异常信息", e);
            throw new BusinessException("下载失败");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
