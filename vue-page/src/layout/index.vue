<template>
  <div class="app-wrapper">
    <!-- 导航栏 -->
    <nav-bar :offlineUserOption="offlineUserOption" @getOfflineUserList="getOfflineUserList"/>

    <div class="app-wrapper-content">
      <!-- 左侧菜单 -->
      <side-bar :navActivate="navActivate" @setNavActivate="setNavActivate"/>

      <!-- 联系人 -->
      <concat @handlerMessageUser="handlerMessageUser" ref="mainContentRef" :navActivate="navActivate"/>

      <!-- 聊天框 -->
      <chatting @sendMessage="sendMessage" ref="contentChatting"/>
    </div>

  </div>
</template>

<script>

import {mapGetters} from "vuex";
import NavBar from "@/layout/components/nav-bar.vue";
import SideBar from "@/layout/components/side-bar.vue";
import Concat from "@/layout/components/concat/index.vue";
import Chatting from "@/layout/components/chatting.vue";
import {getOfflineUserList} from "@/api/sys/user";

export default {
  name: "chat-vue",
  components: {
    Chatting,
    NavBar,
    SideBar,
    Concat
  },
  props: {},
  data() {
    return {
      offlineUserOption: [],
      // 激活的导航菜单:message-消息;contacts-联系人;find-发现;
      navActivate: "message",
      websocket: undefined
    };
  },
  computed: {
    ...mapGetters(["userInfo"])
  },
  watch: {},
  created() {
  },
  mounted() {
    this.getUserInfo();
  },
  methods: {
    // 获取用户信息
    async getUserInfo() {
      if (!this.userInfo.id) {
        await this.getOfflineUserList();
        // 从离线用户里面随机取一个用户
        if (this.offlineUserOption.length > 0) {
          const randomIndex = Math.floor(Math.random() * this.offlineUserOption.length);
          const user = this.offlineUserOption[randomIndex];
          this.$store.dispatch("user/setUserInfo", user);
        }
        // 排除自己
        this.offlineUserOption = this.offlineUserOption.filter(ele => ele.id !== this.userInfo.id);
      }
      // 获取到用户信息后建立连接
      this.connect();
    },
    handlerMessageUser() {
      this.$refs.contentChatting.handlerMessageUser();
    },
    // 获取离线用户列表
    getOfflineUserList() {
      return getOfflineUserList().then(res => {
        if (res.data.code === 200 && res.data.data) {
          this.offlineUserOption = [];
          res.data.data.map(ele => {
            if (this.userInfo && this.userInfo.id !== ele.id) {
              this.offlineUserOption.push(ele);
            }
          });
        }
      });
    },
    // 设置菜单导航
    setNavActivate(navActivate) {
      this.navActivate = navActivate;
    },
    // 建立连接
    connect() {
      // 判断当前浏览器是否支持WebSocket
      if (!("WebSocket" in window)) {
        console.error("不支持WebSocket");
      }
      // websocket对象
      this.websocket = new WebSocket("ws://localhost/chat?userId=" + this.userInfo.id);
      //连接发生错误的回调方法
      this.websocket.onerror = function (e) {
        console.error("WebSocket连接发生错误", e);
      };
      // 连接成功建立的回调方法
      this.websocket.onopen = function () {
        console.log("WebSocket连接成功");
      };
      // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = function () {
        closeWebSocket();
      };

      // 关闭连接
      function closeWebSocket() {
        this.websocket.close();
      }

      // 连接关闭的回调方法
      this.websocket.onclose = function () {
        console.log("WebSocket连接关闭");
      };
      // 接收到消息的回调方法
      this.websocket.onmessage = (event) => {
        console.log("收到消息：", event.data);
        this.$refs.mainContentRef.addMessage(JSON.parse(event.data));  // 调用子组件的方法childClick
      };
    },
    //发送消息
    sendMessage(message) {
      console.log("message:", JSON.stringify(message));
      this.websocket.send(JSON.stringify(message));
    }
  }
};

</script>

<style scoped lang='scss'>
/* 页面flex布局 */
.app-wrapper {
  //flex: 1 2 auto;
  .app-wrapper-content {
    display: flex;
  }
}
</style>