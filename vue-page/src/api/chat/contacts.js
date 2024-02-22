import request from "@/utils/request.js";

/**
 * 分页条件获取联系人列表
 * @returns {*}
 */
export const getByPaged = (data) => {
    return request({
        method: "post",
        url: "/contacts/paged",
        data
    });
};

/**
 * 根据id获取联系人信息
 * @returns {*}
 */
export const getInfoById = (id) => {
    return request({
        method: "get",
        url: `/contacts/info/${id}`
    });
};