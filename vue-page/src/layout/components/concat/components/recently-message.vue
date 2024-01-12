<template>
  <!-- 最近消息列表 -->
  <div class="content-user-list">
    <div class="topping">
      <h3>最近</h3>
    </div>
    <!-- el-scrollbar 必须指定高度 -->
    <el-scrollbar class="content-user-scrollbar">
      <ul class="user-message-list" v-infinite-scroll="handleUserMessageScrollBottom">
        <!-- 倒序 Array.from(recentlyMessageMap.values()).reverse() -->
        <li v-for="(ele,key) in recentlyMessageMap.values()" v-bind:key="key"
            @click="handlerMessageUser(ele)"
            :style="recentlyMessageActivate.id === ele.id ? 'color: #FFA500FF; background-color: #FFA5002A;' : ''"
            class="user-message">
          <img :src="ele.avatar" class="avatar" alt="">
          <div>
            <div class="header">
              <span class="name">{{ ele.name }}</span>
              <span class="create-time"> {{ ele.time }}</span>
            </div>
            <div class="content">
              <div class="message">{{ ele.message }}</div>
              <div class="tip">1</div>
            </div>
          </div>
        </li>
      </ul>
    </el-scrollbar>
  </div>
</template>

<script>

import {mapGetters} from "vuex";

export default {
  name: "recently-message-container",
  components: {},
  props: {},
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["userInfo", "recentlyMessageMap", "recentlyMessageActivate"])
  },
  watch: {},
  created() {
  },
  updated() {

  },
  mounted() {
  },
  beforeDestroy() {
    // bus.$off("getMessageRecordListBus");
  },
  methods: {
    // 最近消息滚动到底触发事件
    handleUserMessageScrollBottom() {
      console.log("111");
    },
    // 获取用户消息列表
    handlerMessageUser(recentlyMessage) {
      if (this.recentlyMessageActivate.id !== recentlyMessage.id) {
        this.$store.dispatch("message/setRecentlyMessageActivate", recentlyMessage);
        // bus.$emit("getMessageRecordListBus");
        this.$emit("handlerMessageUser");
      }
    }
  }
};
</script>

<style scoped lang='scss'>

/* 用户消息列表 */
.content-user-list {
  /* 最近聊天 */
  .topping {
    padding: 20px;
    /* border-bottom: 1 px solid #70809054 !important; */
    border-right: 1px solid #70809054 !important;
    /* box-shadow: 水平阴影 垂直阴影 模糊距离 阴影尺寸 阴影颜色 内外阴影; */
    /* box-shadow: 1 px 0 1 px rgba(0, 0, 0, .075) !important; */
  }

  .content-user-scrollbar {
    overflow-y: auto;
    /* 动态计算盒子的高度 */
    height: calc(100% - 64px);
    border-right: 1px solid #70809054 !important;
    /* box-shadow: 水平阴影 垂直阴影 模糊距离 阴影尺寸 阴影颜色 内外阴影; */
    box-shadow: 1px 0 1px rgba(0, 0, 0, .075) !important;

    .user-message-list {
      display: flex;
      /* 设置主轴的方向 */
      flex-direction: column;

      /* 每个用户消息样式 */
      .user-message {
        position: relative;
        display: flex;
        align-items: center;
        padding: 20px;
        height: 80px;
        border-bottom: 1px solid #70809054 !important;

        /* 用户头像 */
        .avatar {
          margin-right: 20px;
          display: block;
          width: 40px !important;
          height: 40px !important;
          border-radius: 50%;
        }

        /* 用户名称 */
        .name {
          font-size: 16px;
        }

        /* 用户最近消息 */
        .message {
          //margin-top: 5px;
          font-size: 12px;
          /* text-overflow，默认为clip。ellipsis文本溢出时自动加上省略号。 */
          text-overflow: ellipsis;
          // 超出隐藏
          overflow: hidden;
          // 文字不换行
          white-space: nowrap;
          width: 300px;
        }

        /* 消息时间 */
        .create-time {
          position: absolute;
          right: 20px;
          font-size: 12px;
        }

        /* 消息数量 */
        .tip {
          position: absolute;
          left: 45px;
          top: 15px;
          display: block;
          border-radius: 50%;
          background-color: #d53535;
          width: 20px;
          height: 20px;
          line-height: 20px;
          text-align: center;
          vertical-align: middle;
          color: #fff;
        }
      }

      /* 鼠标移入每个用户消息栏样式 */
      .user-message:hover {
        color: #FFA500FF;
        background-color: #FFA5000A;
        cursor: pointer
      }
    }
  }
}
</style>