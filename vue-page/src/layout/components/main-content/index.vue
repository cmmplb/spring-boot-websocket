<template>
  <!-- 内容布局 -->
  <div class="main-content-container">
    <!-- 最近消息列表 -->
    <RecentlyMessage v-if="navActivate === 'message'" @handlerMessageUser="handlerMessageUser"/>

    <!-- 通讯录 -->
    <AddressBook v-if="navActivate === 'addressBook'"/>

  </div>
</template>

<script>

import {mapGetters} from "vuex";
import RecentlyMessage from "./recently-message.vue";
import AddressBook from "./address-book.vue";

export default {
  name: "message-info-container",
  components: {
    RecentlyMessage,
    AddressBook
  },
  props: {},
  data() {
    return {
      load: false,
      // 用户聊天消息列表
      messageMap: {
        // id:[message]
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo", "navActivate", "recentlyMessageMap", "recentlyMessageActivate", "contactsActivate"])
  },
  watch: {},
  created() {
  },
  mounted() {
  },
  updated() {
  },
  methods: {
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
  height: 100%;
  width: 100%;
}
</style>