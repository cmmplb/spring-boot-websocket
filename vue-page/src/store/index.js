import Vue from 'vue';
import Vuex from 'vuex';
import user from './modules/user';
import message from './modules/message';
import getters from './getters';

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        user,
        message,
    },
    getters,
});

export default store;