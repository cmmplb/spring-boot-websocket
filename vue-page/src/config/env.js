// 配置编译环境和线上环境之间的切换

// env: {
//    BASE_URL: "/"
//    NODE_ENV: "development"
// }
const env = process.env;
// 在线引入阿里矢量图==》https://blog.csdn.net/weixin_48802343/article/details/120369457
/* 在线链接服务仅供平台体验和调试使用，平台不承诺服务的稳定性，企业客户需下载字体包自行发布使用并做好备份。 */
// https://at.alicdn.com/t/c/font_3485860_sxzl6ij33pd.css?spm=a313x.7781069.1998910419.53&file=font_3485860_ovlzhbhfd5b.css
const iconfontVersion = ["4415895_0yo2coh32ysp"];//at.alicdn.com/t/c/font_4415895_0yo2coh32ysp.css
const iconfontUrl = `//at.alicdn.com/t/c/font_$key.css`;
let wsUrl;
let baseUrl;
switch (process.env.NODE_ENV) {
    case "development":
        wsUrl = "ws://localhost/chat";
        // baseUrl = "http://localhost:80";
        baseUrl = "/api";
        break;
    case "production":
        wsUrl = "ws://8.130.107.131:8080/ws/chat";
        baseUrl = "http://8.130.107.131:8080/api";
        break;
    default:
        wsUrl = "ws://localhost/chat";
        baseUrl = "/api";
        break;
}
// console.log("process.env.NODE_ENV:", process.env.NODE_ENV);
// console.log("baseUrl", baseUrl);
// console.log("wsUrl", wsUrl);
export {
    env,
    wsUrl,
    baseUrl,
    iconfontVersion,
    iconfontUrl
};