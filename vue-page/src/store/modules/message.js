import {getStorage, removeStorage, setStorage} from "@/utils/storage";
import Vue from "vue";
import {messageMapKey, recentlyMessageActivateKey, recentlyMessageMapKey} from "@/utils/constant";

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

const state = {
    // 激活的最近消息
    recentlyMessageActivate: getStorage({name: recentlyMessageActivateKey}) || {},
    // 最近消息列表,key=用户id,value=最近消息
    recentlyMessageMap: getRecentlyMessageMap(),
    // 用户聊天消息列表s
    messageMap: getStorage({name: messageMapKey}) || {}
};

const mutations = {
    SET_RECENTLY_MESSAGE_MAP: (state, recentlyMessageList) => {
        recentlyMessageList.map(ele => {
            state.recentlyMessageMap.set(ele.businessId, ele);
        });
        setStorage({name: recentlyMessageMapKey, content: recentlyMessageList});
    },
    SET_RECENTLY_MESSAGE: (state, message) => {
        // 删除对象，重新赋值，把聊天用户放到第一位
        state.recentlyMessageMap.delete(message.businessId);
        // const newMap = new Map([...state.recentlyMessageMap.entries()]);
        const newMap = new Map();
        newMap.set(message.businessId, message);
        // 使用forEach()方法遍历原始Map并设置到目标Map中
        state.recentlyMessageMap.forEach((value, key) => {
            newMap.set(key, value);
        });
        // state.recentlyMessageMap.set(message.businessId, recentlyMessage);
        // Vue.set(state.recentlyMessageMap, message.businessId, recentlyMessage);
        // 层级太深，vue监听不到数据变化，直接重新复制一个新对象
        state.recentlyMessageMap = newMap;
        const list = [];
        state.recentlyMessageMap.forEach((val) => {
            list.push(val);
        });
        // 由于map不能存储到本地，这里转换一下，存储的时候存集合，获取转为map
        setStorage({name: recentlyMessageMapKey, content: list});
    },
    SET_RECENTLY_MESSAGE_ACTIVATE: (state, recentlyMessageActivate) => {
        state.recentlyMessageActivate = recentlyMessageActivate;
        setStorage({name: recentlyMessageActivateKey, content: recentlyMessageActivate});
    },
    DEL_RECENTLY_MESSAGE_ACTIVATE: () => {
        state.recentlyMessageActivate = {};
        removeStorage({name: recentlyMessageActivateKey});
    },
    DEL_RECENTLY_MESSAGE_MAP: () => {
        state.recentlyMessageMap.clear();
        removeStorage({name: recentlyMessageMapKey});
    },
    DEL_MESSAGE_MAP: (state) => {
        state.messageMap = {};
        removeStorage({name: messageMapKey});
    },
    SET_MESSAGE_MAP: (state, messageList) => {
        Vue.set(state.messageMap, state.recentlyMessageActivate.businessId, messageList);
        setStorage({name: messageMapKey, content: state.messageMap});
    },
    PUSH_MESSAGE_MAP: (state, message) => {
        state.messageMap[state.recentlyMessageActivate.businessId].push(message);
        setStorage({name: messageMapKey, content: state.messageMap});
    }
};

const actions = {
    setRecentlyMessageMap({commit}, recentlyMessageList) {
        return new Promise((resolve) => {
            commit("SET_RECENTLY_MESSAGE_MAP", recentlyMessageList);
            resolve();
        });
    },
    setRecentlyMessage({commit}, message) {
        return new Promise((resolve) => {
            commit("SET_RECENTLY_MESSAGE", message);
            resolve();
        });
    },
    setRecentlyMessageActivate({commit}, recentlyMessageActivate) {
        return new Promise((resolve) => {
            commit("SET_RECENTLY_MESSAGE_ACTIVATE", recentlyMessageActivate);
            resolve();
        });
    },
    setMessageMap({commit}, messageList) {
        return new Promise((resolve) => {
            commit("SET_MESSAGE_MAP", messageList);
            resolve();
        });
    },
    pushMessageMap({commit}, message) {
        return new Promise((resolve) => {
            commit("PUSH_MESSAGE_MAP", message);
            resolve();
        });
    },
    delRecentlyMessageActivate({commit}) {
        return new Promise((resolve) => {
            commit("DEL_RECENTLY_MESSAGE_ACTIVATE");
            resolve();
        });
    },
    delRecentlyMessageMap({commit}) {
        return new Promise((resolve) => {
            commit("DEL_RECENTLY_MESSAGE_MAP");
            resolve();
        });
    },
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