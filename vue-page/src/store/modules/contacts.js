import {getStorage, removeStorage, setStorage} from "@/utils/storage";
import {contactsActivateKey} from "@/utils/constant";

const state = {
    // 激活的联系人
    contactsActivate: getStorage({name: contactsActivateKey}) || {}
};

const mutations = {
    // 设置联系人激活对象
    SET_CONTACTS_ACTIVATE: (state, contactsActivate) => {
        state.contactsActivate = contactsActivate;
        setStorage({name: contactsActivateKey, content: contactsActivate});
    },
    // 删除最近消息激活对象
    DEL_CONTACTS_ACTIVATE: () => {
        state.contactsActivate = {};
        removeStorage({name: contactsActivateKey});
    }
};

const actions = {
    // 设置联系人激活对象
    setContactsActivate({commit}, contactsActivate) {
        return new Promise((resolve) => {
            commit("SET_CONTACTS_ACTIVATE", contactsActivate);
            resolve();
        });
    },
    // 删除联系人激活对象
    delContactsActivate({commit}) {
        return new Promise((resolve) => {
            commit("DEL_CONTACTS_ACTIVATE");
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