import Vue from 'vue';
import Vuex from 'vuex';
import common from './modules/common';
import user from './modules/user';
import message from './modules/message';
import contacts from './modules/contacts';
import getters from './getters';

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        common,
        user,
        message,
        contacts,
    },
    getters,
});

export default store;