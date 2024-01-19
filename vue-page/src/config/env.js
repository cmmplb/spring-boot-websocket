// 配置编译环境和线上环境之间的切换

// env: {
//    BASE_URL: "/"
//    NODE_ENV: "development"
//    VUE_APP_BASE_API: "/api"
//    VUE_APP_TITLE: "momo-ui"
// }
const env = process.env;
// 在线引入阿里矢量图==》https://blog.csdn.net/weixin_48802343/article/details/120369457
/* 在线链接服务仅供平台体验和调试使用，平台不承诺服务的稳定性，企业客户需下载字体包自行发布使用并做好备份。 */
// https://at.alicdn.com/t/c/font_3485860_sxzl6ij33pd.css?spm=a313x.7781069.1998910419.53&file=font_3485860_ovlzhbhfd5b.css
const iconfontVersion = ["4415895_lcslz17f83"];//at.alicdn.com/t/c/font_4415895_lcslz17f83.css
const iconfontUrl = `//at.alicdn.com/t/c/font_$key.css`;

const codeUrl = `${window.location.origin}${env.VUE_APP_BASE_API}/code`;

export {
    env,
    codeUrl,
    iconfontVersion,
    iconfontUrl
};