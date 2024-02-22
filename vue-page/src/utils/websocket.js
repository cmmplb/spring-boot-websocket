import store from "@/store";
import {wsUrl} from "@/config/env";
import {currentDateTime} from "@/utils/date";

// https://cloud.tencent.com/developer/article/2182509?areaSource=102001.16&traceId=B-1SXkhuXTNsj4vvMRf8A
const websocket = {
    ws: undefined,
    pingTimeout: 30000, // 发送心跳包间隔
    pongTimeout: 75000, // 最长未接收消息的间隔
    reconnectTimeout: 3000, // 每次重连间隔
    reconnectLimit: 3, // 最大重连次数
    pingTimer: undefined, // 心跳包定时器
    pongTimer: undefined, // 接收消息定时器
    reconnectTimer: undefined, // 重连定时器
    reconnectCount: 0, // 当前的重连次数
    lockReconnect: false, // 锁定重连
    lockReconnectTask: false, // 锁定重连任务队列
    createWebSocket() {
        // 判断当前浏览器是否支持WebSocket
        if (!("WebSocket" in window)) {
            console.error("不支持WebSocket");
            return;
        }
        //打印整个state的属性
        const userInfo = store.state.user.userInfo;
        if (!userInfo) {
            console.log("未查询到用户信息");
            return;
        }
        this.ws = new WebSocket(wsUrl + "?userId=" + userInfo.id);
        // 连接成功建立的回调方法
        this.ws.onopen = () => {
            console.log("连接成功");
            this.clearAllTimer();
            this.heartBeat();
            // 解锁，可以重连
            this.lockReconnect = false;
            // 连接成功后重置重连次数
            this.reconnectCount = 0;
        };
        // 连接关闭的回调方法
        this.ws.onclose = () => {
            console.log("连接已关闭");
            this.readyReconnect();
        };
        // 连接发生错误的回调方法
        this.ws.onerror = (e) => {
            console.log("连接发生错误", e);
            this.readyReconnect();
        };
        // 接收到消息的回调方法
        this.ws.onmessage = (e) => {
            const data = JSON.parse(e.data);
            // 消息类型:1-心跳;2-聊天
            if (data.type === 1) {
                // 心跳
                console.log("onmessage heartbeat,", currentDateTime());
            } else if (data.type === 2) {
                window.onMessage(data.message);
            } else if (data.type === 3 || data.type === 4) {
                // 消息已读
                window.onReadMessage(data);
            }
            // 超时定时器
            clearTimeout(this.pongTimer);
            this.pongTimer = setTimeout(() => {
                this.readyReconnect();
            }, this.pongTimeout);

        };
    },
    // 发送消息
    send(message) {
        if (this.isOpen()) {
            this.ws.send(JSON.stringify({type: 2, message: message}));
        }
    },
    isOpen() {
        return this.ws.readyState === this.ws.OPEN;
    },
    // 关闭连接
    close() {
        console.log("关闭连接");
        this.ws.close();
    },
    // 发送心跳包
    heartBeat() {
        this.pingTimer = setTimeout(() => {
            console.log("send heartbeat,", currentDateTime());
            if (this.isOpen()) {
                this.ws.send(JSON.stringify({type: 1}));
            }
            this.heartBeat();
        }, this.pingTimeout);
    },
    // 准备重连
    readyReconnect() {
        // 避免循环重连，当一个重连任务进行时，不进行重连
        if (this.lockReconnectTask) return;
        this.lockReconnectTask = true;
        this.clearAllTimer();
        this.reconnect();
    },
    // 重连
    reconnect() {
        if (this.lockReconnect) return;
        if (this.reconnectCount > this.reconnectLimit) return;
        // 加锁，禁止重连
        this.lockReconnect = true;
        console.log(`重连第${this.reconnectCount}次`);
        this.reconnectCount++;
        this.createWebSocket();
        this.reconnectTimer = setTimeout(() => {
            // 解锁，可以重连
            this.lockReconnect = false;
            this.reconnect();
        }, this.reconnectTimeout);
    },
    // 清空所有定时器
    clearAllTimer() {
        clearTimeout(this.pingTimer);
        clearTimeout(this.pongTimer);
        clearTimeout(this.reconnectTimer);
    }
};
export default websocket;
