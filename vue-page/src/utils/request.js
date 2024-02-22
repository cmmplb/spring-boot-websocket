// 导入axios实例
import axios from 'axios';
import { buildMsg } from '@/utils/message';
import { getStorage } from '@/utils/storage';
import {baseUrl} from "@/config/env";

// 创建了一个新的axios实例
const service = axios.create({
    baseURL: baseUrl,
    // baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
    // baseURL: "http://localhost:9000/" , // url = base url + request url
    // withCredentials: true, // send cookies when cross-domain requests, 跨域请求，允许保存cookie
    timeout: 60000, // 超时时间
});

// 配置请求拦截器
service.interceptors.request.use(
    // todo:
    config => {
        const currentUser = getStorage({name: 'currentUser'});
        if (currentUser) {
            config.headers['User-Id'] = currentUser.id;
        }
        return config;
    },
    error => {
        // do something with request error
        // console.log('request.js=>request=>error:', error); // for debug
        return Promise.reject(error);
    },
);

// 配置响应拦截器
service.interceptors.response.use(
    response => {
        if (response.status !== 200) {
            buildMsg(response.data.msg || '服务器繁忙1');
        }
        return response;
    },
    error => {
        // 登录失效
        if (error && error.response && error.response.status === 401) {
            // buildMsg('登录失效', 'info');
            // 记录当前页面地址，用于登录成功回调
            let refer = window.location.href;
            if (refer.indexOf('login') === -1) {
                console.log('refer:', refer);
                // setStorage({name: storage.referPrefix, content: refer, type: storage.session});
            }
            // store.dispatch('user/clear').then(() => {
            //     // 存储当前页面，用于登录成功后重定向到当前页
            //     if (window.location.pathname !== '/login') {
            //         window.location.href = '/login';
            //     }
            // });
            buildMsg('登陆失效');
            return Promise.reject(new Error(error));
        }
        // 其他异常
        if (error && error.response && error.response.status !== 200) {
            if (error.response.data.msg) {
                buildMsg(error.response.data.msg, 'error');
            } else {
                buildMsg('服务器繁忙');
            }
        }
        return Promise.reject(new Error(error));
    },
);


export default service; // 导出axios实例