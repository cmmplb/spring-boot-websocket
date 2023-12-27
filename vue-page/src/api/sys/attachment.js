import request from '@/utils/request.js';

export const upload = (data) => {
    return request({
        method: 'post',
        url: '/attachment/upload',
        data,
    });
};