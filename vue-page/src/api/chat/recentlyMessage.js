import request from '@/utils/request.js';

/**
 * 分页条件获取最近消息列表
 * @returns {*}
 */
export const getByPaged = (data) => {
    return request({
        method: 'post',
        url: '/recently/message/paged',
        data
    });
};