import {getStorage, removeStorage, setStorage} from "@/utils/storage";
import {messageMapKey, messageUnReadMapKey, recentlyMessageActivateKey, recentlyMessageMapKey} from "@/utils/constant";

const getRecentlyMessageMap = () => {
    // const obj = Object.fromEntries(map); // Map转对象
    // const map = new Map(Object.entries(obj)); // 对象转map :
    const recentlyMessageMap = new Map();
    // 由于map不能存储到本地，这里转换一下，存储的时候存集合，获取转为map
    const recentlyMessageList = getStorage({name: recentlyMessageMapKey});
    if (recentlyMessageList && recentlyMessageList.length > 0) {
        recentlyMessageList.map(ele => {
            recentlyMessageMap.set(ele.businessId, ele);
        });
    }
    return recentlyMessageMap;
};

const getMessageUnReadMap = () => {
    const messageUnReadMap = new Map();
    const messageUnReadList = getStorage({name: messageUnReadMapKey});
    if (messageUnReadList && messageUnReadList.length > 0) {
        messageUnReadList.map(ele => {
            messageUnReadMap.set(ele.businessId, ele.count);
        });
    }
    return messageUnReadMap;
};
const getMessageMap = () => {
    const messageMap = new Map();
    const messageList = getStorage({name: messageMapKey});
    if (messageList && messageList.length > 0) {
        messageList.map(ele => {
            messageMap.set(ele.businessId, ele);
        });
    }
    return messageMap;
};

// map层级太深，vue监听不到数据变化，重新复制一个新Map
const setMap = (oldMap, newKey, newValue, cacheKey) => {
    const newMap = new Map();
    // 设置新值排序第一位
    newMap.set(newKey, newValue);
    const list = [];
    if (newValue instanceof Object) {
        list.push(newValue);
    } else {
        list.push({newKey, newValue});
    }
    oldMap.delete(newKey);
    // 使用forEach()方法遍历原始Map并设置到目标Map中
    oldMap.forEach((value, key) => {
        if (value instanceof Object) {
            list.push(value);
        } else {
            list.push({key, value});
        }
        newMap.set(key, value);
    });
    setStorage({name: cacheKey, content: list});
    return newMap;
};

const state = {
    // 激活的最近消息
    recentlyMessageActivate: getStorage({name: recentlyMessageActivateKey}) || {},
    // 最近消息列表,key=businessId,value=最近消息
    recentlyMessageMap: getRecentlyMessageMap(),
    // 最消息未读数量,key=businessId,value=数量
    messageUnReadMap: getMessageUnReadMap(),
    // 用户聊天消息列表
    messageMap: getMessageMap()
};

const mutations = {
    // 设置最近消息Map
    SET_RECENTLY_MESSAGE_MAP: (state, recentlyMessageList) => {
        recentlyMessageList.map(ele => {
            state.recentlyMessageMap.set(ele.businessId, ele);
        });
        setStorage({name: recentlyMessageMapKey, content: recentlyMessageList});
    },
    // 设置最近消息
    SET_RECENTLY_MESSAGE: (state, message) => {
        state.recentlyMessageMap = setMap(state.recentlyMessageMap, message.businessId, message, recentlyMessageMapKey);
    },
    // 设置未读消息数量Map
    SET_MESSAGE_UN_READ_MAP: (state, messageCountList) => {
        const messageUnReadList = [];
        messageCountList.map(ele => {
            if (ele.count) {
                state.messageUnReadMap.set(ele.businessId, ele.count);
                messageUnReadList.push({
                    businessId: ele.businessId,
                    count: ele.count
                });
            }
        });
        setStorage({name: messageUnReadMapKey, content: messageUnReadList});
    },
    // 设置消息未读数量
    SET_MESSAGE_UN_READ: (state, businessId) => {
        let count = state.messageUnReadMap.has(businessId) ? state.messageUnReadMap.get(businessId) + 1 : 1;
        state.messageUnReadMap = setMap(state.messageUnReadMap, businessId, count, messageUnReadMapKey);
    },
    // 更新单条消息状态
    SET_MESSAGE_RECORD_READ: (state, data) => {
        const messageList = state.messageMap.get(data.businessId);
        if (messageList && messageList.length > 0) {
            for (let i = 0; i < messageList.length; i++) {
                const ele = messageList[i];
                if (ele.uuid === data.uuid) {
                    // 设置消息已读
                    ele.status = 1;
                    break;
                }
            }
            state.messageMap = setMap(state.messageMap, data.businessId, messageList, messageMapKey);
        }
    },
    // 更新所有消息状态
    SET_ALL_MESSAGE_RECORD_READ: (state, data) => {
        const messageList = state.messageMap.get(data.businessId);
        if (messageList && messageList.length > 0) {
            messageList.map(ele => {
                // 设置消息已读
                ele.status = 1;
            });
            state.messageMap = setMap(state.messageMap, data.businessId, messageList, messageMapKey);
        }
    },
    // 读取用户所有未读消息数量
    READ_USER_ALL_MESSAGE: (state, businessId) => {
        state.messageUnReadMap.delete(businessId);
        setStorage({name: messageUnReadMapKey, content: state.messageUnReadMap});
    },
    // 设置最近消息激活对象
    SET_RECENTLY_MESSAGE_ACTIVATE: (state, recentlyMessageActivate) => {
        state.recentlyMessageActivate = recentlyMessageActivate;
        setStorage({name: recentlyMessageActivateKey, content: recentlyMessageActivate});
    },
    // 删除最近消息激活对象
    DEL_RECENTLY_MESSAGE_ACTIVATE: () => {
        state.recentlyMessageActivate = {};
        removeStorage({name: recentlyMessageActivateKey});
    },
    // 删除最近消息
    DEL_RECENTLY_MESSAGE_MAP: () => {
        state.recentlyMessageMap.clear();
        removeStorage({name: recentlyMessageMapKey});
    },
    // 删除消息记录
    DEL_MESSAGE_MAP: (state) => {
        state.messageMap.clear();
        removeStorage({name: messageMapKey});
    },
    // 设置消息记录Map
    SET_MESSAGE_MAP: (state, messageList) => {
        state.messageMap = setMap(state.messageMap, state.recentlyMessageActivate.businessId, messageList, messageMapKey);
    },
    // 添加消息记录
    PUSH_MESSAGE_MAP: (state, message) => {
        const messageList = state.messageMap.has(state.recentlyMessageActivate.businessId) ?
            state.messageMap.get(state.recentlyMessageActivate.businessId) : [];
        messageList.push(message);
        state.messageMap = setMap(state.messageMap, state.recentlyMessageActivate.businessId, messageList, messageMapKey);
    }
};

const actions = {
    // 设置最近消息Map
    setRecentlyMessageMap({commit}, recentlyMessageList) {
        return new Promise((resolve) => {
            commit("SET_RECENTLY_MESSAGE_MAP", recentlyMessageList);
            resolve();
        });
    },
    // 设置最近消息
    setRecentlyMessage({commit}, message) {
        return new Promise((resolve) => {
            commit("SET_RECENTLY_MESSAGE", message);
            resolve();
        });
    },
    // 设置未读消息数量Map
    setMessageUnReadMap({commit}, messageCountList) {
        return new Promise((resolve) => {
            commit("SET_MESSAGE_UN_READ_MAP", messageCountList);
            resolve();
        });
    },
    // 设置消息未读数量
    setMessageUnRead({commit}, businessId) {
        return new Promise((resolve) => {
            commit("SET_MESSAGE_UN_READ", businessId);
            resolve();
        });
    },
    // 读取用户所有未读消息数量
    readUserAllMessage({commit}, businessId) {
        return new Promise((resolve) => {
            commit("READ_USER_ALL_MESSAGE", businessId);
            resolve();
        });
    },
    // 更新单条消息状态
    setMessageRecordRead({commit}, data) {
        return new Promise((resolve) => {
            commit("SET_MESSAGE_RECORD_READ", data);
            resolve();
        });
    },
    // 更新所有消息状态
    setAllMessageRecordRead({commit}, data) {
        return new Promise((resolve) => {
            commit("SET_ALL_MESSAGE_RECORD_READ", data);
            resolve();
        });
    },
    // 设置最近消息激活对象
    setRecentlyMessageActivate({commit}, recentlyMessageActivate) {
        return new Promise((resolve) => {
            commit("SET_RECENTLY_MESSAGE_ACTIVATE", recentlyMessageActivate);
            resolve();
        });
    },
    // 设置消息记录Map
    setMessageMap({commit}, messageList) {
        return new Promise((resolve) => {
            commit("SET_MESSAGE_MAP", messageList);
            resolve();
        });
    },
    // 添加消息记录
    pushMessageMap({commit}, message) {
        return new Promise((resolve) => {
            commit("PUSH_MESSAGE_MAP", message);
            resolve();
        });
    },
    // 删除最近消息激活对象
    delRecentlyMessageActivate({commit}) {
        return new Promise((resolve) => {
            commit("DEL_RECENTLY_MESSAGE_ACTIVATE");
            resolve();
        });
    },
    // 删除最近消息
    delRecentlyMessageMap({commit}) {
        return new Promise((resolve) => {
            commit("DEL_RECENTLY_MESSAGE_MAP");
            resolve();
        });
    },
    // 删除消息记录
    delMessageMap({commit}) {
        return new Promise((resolve) => {
            commit("DEL_MESSAGE_MAP");
            resolve();
        });
    }
};

export default {
    namespaced: true,
    state,
    mutations,
    actions
};