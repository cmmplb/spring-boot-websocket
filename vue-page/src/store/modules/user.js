import { getStorage, setStorage } from '@/utils/storage';

const state = {
    userInfo: getStorage({name: 'currentUser'}) || {},
};

const mutations = {
    SET_USER_INFO: (state, userInfo) => {
        state.userInfo = userInfo;
        setStorage({name: 'currentUser', content: userInfo});
    },
};

const actions = {
    // 设置当前用户
    setUserInfo({commit}, userInfo) {
        return new Promise((resolve/*, reject*/) => {
            commit('SET_USER_INFO', userInfo);
            resolve();
        });
    },
};

export default {
    // 开启命名空间后调用需要在前面指定模块名,store.dispatch('user/***')
    namespaced: true,
    state,
    mutations,
    actions,
};