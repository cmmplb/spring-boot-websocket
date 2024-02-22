import request from "@/utils/request.js";

/**
 * 获取用户/群消息列表
 * @returns {*}
 */
export const getByPaged = (data) => {
    return request({
        method: "post",
        url: "/message/record/paged",
        data
    });
};

/**
 * 标记单条消息已读
 * @returns {*}
 */
export const read = (uuid) => {
    return request({
        method: "post",
        url: `/message/record/read/${uuid}`
    });
};

/**
 * 标记用户/群消息已读
 * @returns {*}
 */
export const readBusiness = (businessId, userId) => {
    return request({
        method: "post",
        url: `/message/record/read/business/${businessId}/${userId}`
    });
};