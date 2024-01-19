<template>
  <div class="app-wrapper">
    <!-- 导航栏 -->
    <NavBar :offlineUserOption="offlineUserOption" @getOfflineUserList="getOfflineUserList"/>

    <div class="app-wrapper-content">
      <!-- 左侧菜单 -->
      <SideBar :navActivate="navActivate" @setNavActivate="setNavActivate"/>

      <!-- 通讯录 -->
      <AddressBook @handlerMessageUser="handlerMessageUser" :navActivate="navActivate"/>

      <!-- 聊天框 -->
      <ChatBox ref="chatBoxRef" v-if="navActivate === 'message' && recentlyMessageActivate.businessId"/>
    </div>

  </div>
</template>

<script>

import {mapGetters} from "vuex";
import NavBar from "@/layout/components/nav-bar.vue";
import SideBar from "@/layout/components/side-bar.vue";
import AddressBook from "@/layout/components/address-book/index.vue";
import ChatBox from "@/layout/components/chat-box/index.vue";
import {getOfflineUserList} from "@/api/sys/user";
import websocket from "@/utils/websocket";


export default {
  name: "chat-vue",
  components: {
    ChatBox,
    NavBar,
    SideBar,
    AddressBook
  },
  props: {},
  data() {
    return {
      offlineUserOption: [],
      // 激活的导航菜单:message-消息;contacts-联系人;find-发现;
      navActivate: "message"
    };
  },
  computed: {
    ...mapGetters(["userInfo", "recentlyMessageMap", "recentlyMessageActivate"])
  },
  watch: {},
  created() {
  },
  mounted() {
    this.getUserInfo();
  },
  beforeDestroy() {
    window.addEventListener("beforeunload", this.handleWindowClose); // 添加监听器
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
          await this.$store.dispatch("user/setUserInfo", user);
        }
        // 排除自己
        this.offlineUserOption = this.offlineUserOption.filter(ele => ele.id !== this.userInfo.id);
      }
      // 获取到用户信息后建立连接
      websocket.init(this.userInfo.id);
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
    // 点击最近消息用户
    handlerMessageUser() {
      this.$nextTick(() => {
        if (this.$refs.chatBoxRef) {
          this.$refs.chatBoxRef.handlerMessageUser();
        }
      });
    },
    // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    handleWindowClose(event) {
      event.preventDefault(); // 取消默认行为
      websocket.close();
    }
  }
};

</script>

<style scoped lang='scss'>
/* 页面flex布局 */
.app-wrapper {
  .app-wrapper-content {
    display: flex;
  }
}
</style>