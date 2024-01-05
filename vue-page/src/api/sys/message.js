import request from '@/utils/request.js';

/**
 * 获取最近消息列表
 * @returns {*}
 */
export const getRecentlyMessageList = () => {
    return request({
        method: 'get',
        url: '/message/recently/list'
    });
};

/**
 * 获取用户/群消息列表
 * @returns {*}
 */
export const getMessageList = (data) => {
    return request({
        method: 'post',
        url: '/message/paged',
        data
    });
};