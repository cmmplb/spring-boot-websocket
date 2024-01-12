import {validateNull} from "@/utils/validate";

/**
 * 获取localStorage
 * @returns {string|any}
 * @param params
 */
export const getStorage = (params = {}) => {
    let {
        name,
        type
    } = params;
    let obj, content;
    if (type === "session") {
        obj = window.sessionStorage.getItem(name);
    } else {
        obj = window.localStorage.getItem(name);
    }
    try {
        obj = JSON.parse(obj);
    } catch (err) {
        return obj;
    }
    if (validateNull(obj)) {
        return obj;
    }
    if (obj.dataType === "string") {
        content = obj.content;
    } else if (obj.dataType === "number") {
        content = Number(obj.content);
    } else if (obj.dataType === "boolean") {
        content = eval(obj.content);
    } else if (obj.dataType === "object") {
        content = obj.content;
    }
    return content;
};

/**
 * 存储localStorage
 * @param params
 */
export const setStorage = (params = {}) => {
    let {
        name,
        content,
        type
    } = params;
    let obj = {
        dataType: typeof (content),
        content: content,
        type: type,
        datetime: new Date().getTime()
    };
    obj = JSON.stringify(obj);
    if (type === "session") {
        window.sessionStorage.setItem(name, obj);
    } else {
        window.localStorage.setItem(name, obj);
    }
};

/**
 * 删除localStorage
 * @param params
 */
export const removeStorage = (params = {}) => {
    let {
        name,
        type
    } = params;
    if (type === "session") {
        window.sessionStorage.removeItem(name);
    } else {
        window.localStorage.removeItem(name);
    }
};

/**
 * 清空全部localStorage
 */
export const clearStorage = (params = {}) => {
    let {type} = params;
    if (type === "session") {
        window.sessionStorage.clear();
    } else {
        window.localStorage.clear();
    }

};