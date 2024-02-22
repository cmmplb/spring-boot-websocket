import Vue from "vue";
import App from "./App.vue";

// 加载环境变量
import * as envs from "@/config/env";
import {iconfontUrl, iconfontVersion} from "@/config/env";
import {loadStyle} from "@/utils/util";

import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
// 加载index样式
import "./assets/styles/common.css";

// store
import store from "./store";


// 是否显示提示和警告信息
Vue.config.productionTip = false;

// ElementUI
Vue.use(ElementUI);

// 配置全局属性=> this.${name} 调用
Object.keys(envs).forEach(e => {
    Vue.prototype[e] = envs[e];
});

// 动态加载阿里云字体库
iconfontVersion.forEach(ele => {
    loadStyle(iconfontUrl.replace("$key", ele));
});

new Vue({
    el: "#app",
    store,
    render: h => h(App)
});