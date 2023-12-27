import Vue from 'vue';
import App from './App.vue';

// 是否显示提示和警告信息
Vue.config.productionTip = false;

// 加载index样式
import './assets/styles/common.css';

// store
import store from './store';

// ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);



new Vue({
    el: '#app',
    store,
    render: h => h(App),
});
