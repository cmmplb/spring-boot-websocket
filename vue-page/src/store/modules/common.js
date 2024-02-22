import {getStorage, setStorage} from "@/utils/storage";
import {navActivateKey} from "@/utils/constant";

const state = {
    // 激活的导航菜单:message-消息;addressBook-通讯录;find-发现;
    navActivate: getStorage({name: navActivateKey}) || ""
};

const mutations = {
    // 设置激活导航菜单
    SET_NAV_ACTIVATE: (state, navActivate) => {
        state.navActivate = navActivate;
        setStorage({name: navActivateKey, content: navActivate});
    }
};

const actions = {
    // 设置联系人激活对象
    setNavActivate({commit}, navActivate) {
        return new Promise((resolve) => {
            commit("SET_NAV_ACTIVATE", navActivate);
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