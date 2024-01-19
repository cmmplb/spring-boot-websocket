import request from '@/utils/request.js';

/**
 * 获取用户/群消息列表
 * @returns {*}
 */
export const getByPaged = (data) => {
    return request({
        method: 'post',
        url: '/message/record/paged',
        data
    });
};