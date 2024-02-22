// const CompressionPlugin = require('compression-webpack-plugin')
const path = require("path");

// 配置@为src根目录
function resolve(dir) {
    return path.join(__dirname, dir);
}

// 页面标题 title
const name = process.env.VUE_APP_TITLE || "chat";

// port
const port = process.env.port || process.env.npm_config_port || 1234;

// vue.config.js 配置说明
// 官方vue.config.js 参考文档 https://cli.vuejs.org/zh/config/#css-loaderoptions
module.exports = {
    // 部署生产环境和开发环境下的URL。
    // 默认情况下，Vue CLI 会假设你的应用是被部署在一个域名的根路径上
    // 例如 https://www.ruoyi.vip/。如果应用被部署在一个子路径上，你就需要用这个选项指定这个子路径。例如，如果你的应用被部署在 https://www.ruoyi.vip/admin/，则设置 baseUrl 为 /admin/。
    // publicPath: process.env.PUBLIC_PATH ? process.env.PUBLIC_PATH : "/",
    publicPath: "./",
    // 在npm run build 或 yarn build 时 ，生成文件的目录名称（要和baseUrl的生产环境路径一致）（默认dist）
    outputDir: "dist",
    // 用于放置生成的静态资源 (js、css、img、fonts) 的；（项目打包之后，静态资源会放在这个文件夹下）
    assetsDir: "static",
    // 是否开启eslint保存检测，有效值：ture | false | 'error',开发环境关闭eslint检查
    lintOnSave: process.env.NODE_ENV === "development",
    // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
    productionSourceMap: false,
    // webpack-dev-server 相关配置
    devServer: {
        // history模式下的url会请求到服务器端，但是服务器端并没有这一个资源文件，就会返回404，所以需要配置这一项
        historyApiFallback: {
            index: "/index.html" //与output的publicPath
        },
        // host: '0.0.0.0',
        port: port,
        // 启动成功后打开页面
        open: false,
        // 本地服务代理
        proxy: {
            // 代理的请求前缀 ['/api']
            "/api": {
                target: `http://localhost:80`,
                changeOrigin: true,
                // 是否开启 webSocket 代理
                ws: true,
                pathRewrite: {
                    // 当请求地址中以"/api"开头的地址替换成空字符串
                    "^/api": "/"
                }
            }
        }
    },

    // 配置Webpack
    configureWebpack: {
        name: name,
        resolve: {
            alias: {
                // 配置@为src根目录
                "@": resolve("src")
            }
        }
    },
    css: {
        loaderOptions: {
            scss: {
                additionalData: `@import "@/assets/styles/variables.scss";`
            }
        }
    }
};