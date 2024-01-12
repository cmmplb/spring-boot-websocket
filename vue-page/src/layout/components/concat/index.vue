<template>
  <!-- 内容布局 -->
  <div class="main-content-container">
    <!-- 最近消息列表 -->
    <recently-message v-if="navActivate === 'message' && this.recentlyMessageMap.size > 0"
                      @handlerMessageUser="handlerMessageUser"
    />

    <!-- 联系人列表 -->
    <!--    <div class="concat-user-list" v-if="navActivate === 'contacts'">
          <el-scrollbar class="user-scrollbar">
            <ul class="user-message-list">
              <li v-for="ele in recentlyMessageMap" v-bind:key="ele.id" class="user">
                <div class="user-avatar"></div>
                <span>{{ ele.name }}</span>
              </li>
            </ul>
          </el-scrollbar>
        </div>-->
  </div>
</template>

<script>

import {getRecentlyMessageByPaged} from "@/api/chat/recentlyMessage";
import {mapGetters} from "vuex";
import RecentlyMessage from "./components/recently-message.vue";

export default {
  name: "concat-container",
  components: {
    RecentlyMessage
  },
  props: {
    navActivate: {
      type: String
    }
  },
  data() {
    return {
      // 最近消息分页条件
      pageData: {
        size: 10,
        current: 1,
        total: 0,
        totalPage: 0
      },
      load: false,
      // 用户聊天消息列表
      messageMap: {
        // id:[message]
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo", "recentlyMessageMap", "recentlyMessageActivate"])
  },
  watch: {},
  created() {
  },
  mounted() {
    // 获取最近消息列表
    if (this.userInfo.id) {
      this.getRecentlyMessageList();
    }
  },
  updated() {
    if (this.messageMap) {
      console.log("this.messageMap:", this.messageMap);
    }
    if (this.recentlyMessageMap) {
      console.log("messageUserMap:", this.recentlyMessageMap);
    }
    if (this.recentlyMessageActivate) {
      console.log("messageUserActivate:", this.recentlyMessageActivate);
    }
  },
  methods: {
    // 获取最近消息列表
    getRecentlyMessageList() {
      getRecentlyMessageByPaged({...this.pageData}).then(res => {
        if (res.data.code === 200 && res.data.data) {
          this.pageData.total = res.data.data.total;
          this.pageData.totalPage = Math.ceil(this.pageData.total / this.pageData.size);
          this.$store.dispatch("message/setRecentlyMessageMap", res.data.data.rows);
          const keys = this.recentlyMessageMap.keys();
          if (!this.recentlyMessageActivate.id && this.recentlyMessageMap && this.recentlyMessageMap.size > 0) {
            this.$store.dispatch("message/setRecentlyMessageActivate", this.recentlyMessageMap.get(keys.next().value));
          }
          this.$forceUpdate();
        }
      });
    },
    // 收到的消息
    addMessage(data) {
      data.send = data.receiveBusinessId !== this.userInfo.id ? 1 : 2;
      // 如果收到的消息是当前聊天用户，把消息添加到聊天对话中
      if (this.recentlyMessageActivate.businessId === data.sendBusinessId) {
        this.$store.dispatch("message/pushMessageMap", data);
      }
      data.businessId = data.sendBusinessId;
      // 把消息更新到最近聊天用户的消息记录上
      this.$store.dispatch("message/setRecentlyMessage", data);
      this.$forceUpdate();
    },
    // 使用bus事件只能在mounted调用了一次，这里单独传递
    handlerMessageUser() {
      this.$emit("handlerMessageUser");
    }

  }
};
</script>

<style scoped lang='scss'>
/* 内容样式 */
.main-content-container {
  display: flex;
}
</style>