import request from '@/utils/request.js';

/**
 * 获取用户信息
 * @returns {*}
 */
export const getInfo = () => {
    return request({
        method: 'get',
        url: '/user/info',
    });
};

/**
 * 修改用户头像
 * @param attachmentId 附件id
 * @returns {*}
 */
export const updateAvatar = (attachmentId) => {
    return request({
        method: 'put',
        url: `/user/avatar/${attachmentId}`,
    });
};

/**
 * 获取用户列表
 * @returns {*}
 */
export const getList = () => {
    return request({
        method: 'get',
        url: '/user/list',
    });
};

/**
 * 获取离线用户列表
 * @returns {*}
 */
export const getOfflineUserList = () => {
    return request({
        method: 'get',
        url: '/user/offline/list',
    });
};