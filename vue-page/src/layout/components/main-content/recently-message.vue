<template>
  <!-- 最近消息列表 -->
  <div class="content-user-list-container">
    <div class="topping bdr">
      <el-input class="topping-search" v-model="searchForm.keywords" prefix-icon="el-icon-search" clearable
                placeholder="搜索"></el-input>
    </div>
    <template v-if="recentlyMessageMap && recentlyMessageMap.size > 0">
      <!-- el-scrollbar 必须指定高度 -->
      <el-scrollbar class="content-scrollbar bdr">
        <ul class="user-list" v-infinite-scroll="handleScrollBottom">
          <!-- 倒序 Array.from(recentlyMessageMap.values()).reverse() -->
          <li v-for="(ele,key) in recentlyMessageMap.values()" v-bind:key="key"
              @click="handlerMessageUser(ele)"
              :style="recentlyMessageActivate.businessId === ele.businessId ? 'color: #FFA500FF; background-color: #FFA5002A;' : ''"
              class="user-message flex bdb">
            <img :src="ele.avatar" class="avatar" alt="">
            <div class="user-message-info">
              <div class="header">
                <span class="name">{{ ele.name }}</span>
                <span class="create-time"> {{ ele.recentlyTime }}</span>
              </div>
              <div class="content">
                <div class="message">{{ ele.message }}</div>
                <div v-if="messageUnReadMap.get(ele.businessId)" class="tip">
                  {{ messageUnReadMap.get(ele.businessId) }}
                </div>
              </div>
            </div>
          </li>
        </ul>
      </el-scrollbar>
    </template>
    <template v-else>
      <div class="none-message flex bdr">
        暂无消息
      </div>
    </template>

    <!-- 聊天框 -->
    <ChatBox ref="chatBoxRef" v-if="navActivate === 'message' && recentlyMessageActivate.businessId"/>
  </div>
</template>

<script>

import {mapGetters} from "vuex";
import {getByPaged} from "@/api/chat/recentlyMessage";
import {readBusiness} from "@/api/chat/messageRecord";
import ChatBox from "@/layout/components/main-content/components/chat-box.vue";

export default {
  name: "recently-message-container",
  components: {ChatBox},
  props: {},
  data() {
    return {
      // 分页条件
      pageData: {
        size: 10,
        current: 1,
        total: 0,
        totalPage: 0
      },
      searchForm: {
        keywords: ""
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo", "navActivate", "recentlyMessageMap", "messageUnReadMap", "recentlyMessageActivate"])
  },
  watch: {},
  created() {
  },
  updated() {

  },
  mounted() {
    // 获取最近消息列表
    if (this.userInfo.id) {
      this.getRecentlyMessageList();
    }
  },
  beforeDestroy() {
    // bus.$off("getMessageRecordListBus");
  },
  methods: {
    // 获取最近消息列表
    getRecentlyMessageList() {
      getByPaged({...this.searchForm, ...this.pageData}).then(res => {
        if (res.data.code === 200 && res.data.data) {
          this.pageData.total = res.data.data.total;
          this.pageData.totalPage = Math.ceil(this.pageData.total / this.pageData.size);
          if (res.data.data.total > 0) {
            // 设置最近消息Map
            this.$store.dispatch("message/setRecentlyMessageMap", res.data.data.rows);
            // 设置未读消息数量Map
            this.$store.dispatch("message/setMessageUnReadMap", res.data.data.rows);
            // 设置默认激活第一个最近聊天消息窗口
            // const keys = this.recentlyMessageMap.keys();
            // if (!this.recentlyMessageActivate.id && this.recentlyMessageMap && this.recentlyMessageMap.size > 0) {
            //   this.$store.dispatch("message/setRecentlyMessageActivate", this.recentlyMessageMap.get(keys.next().value));
            // }
          }
          this.$nextTick(() => {
            this.$forceUpdate();
          });
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    // 滚动到底触发事件
    handleScrollBottom() {
      // console.log("111");
    },
    // 点击用户消息
    handlerMessageUser(recentlyMessage) {
      if (this.recentlyMessageActivate.businessId !== recentlyMessage.businessId) {
        // 设置当前激活用户对象
        this.$store.dispatch("message/setRecentlyMessageActivate", recentlyMessage);
        // 标记用户/群消息已读
        if (this.messageUnReadMap.get(recentlyMessage.businessId)) {
          this.$store.dispatch("message/readUserAllMessage", recentlyMessage.businessId);
          readBusiness(recentlyMessage.businessId, this.userInfo.id);
        }
        // 事件总线
        // bus.$emit("getMessageRecordListBus");
        this.$emit("handlerMessageUser");
      }
    }
  }
};
</script>

<style scoped lang='scss'>

/* 用户消息列表 */
.content-user-list-container {
  display: flex;
  /*  默认情况下，项目都排在一条线（又称“轴线”）上。如果一条轴线排不下，flex-wrap:wrap 该样式用于设置换行 */
  flex-wrap: wrap;
  /* align-content:flex-start 该样式用于让div与交叉轴的起点对齐（即顶部对齐） */
  align-content: flex-start;
  flex-direction: column;
  height: 100%;
  width: 100%;

  /* 搜索 */
  .topping {
    padding: 20px;
    width: $messageWidth;

    ::v-deep .el-input__inner {
      background-color: rgba(112, 128, 144, 0.05);
    }
  }

  /* 暂无消息 */
  .none-message {
    /* 填充整个布局 */
    flex: 1;
    /* 水平居中对齐 */
    justify-content: center;
    width: $messageWidth;
  }

  /* 最近消息列表 */
  .content-scrollbar {
    /* 填充整个布局 */
    flex: 1;
    width: $messageWidth;
    overflow-y: auto;
    /* 动态计算盒子的高度总高度-nav-bar的高度-搜索栏的高度 */
    //height: calc(100% - 80px);

    .user-list {
      display: flex;
      /* 设置主轴的方向 */
      flex-direction: column;

      /* 每个用户消息样式 */
      .user-message {
        position: relative;
        align-items: center;
        padding: 20px;
        height: 80px;

        /* 用户头像 */
        .avatar {
          margin-right: 20px;
          display: block;
          width: 40px !important;
          height: 40px !important;
          border-radius: 50%;
        }


        .user-message-info {
          width: 77%;
          flex: 1;

          .header {
            /* 用户名称 */
            .name {
              //width: 62%;
              font-size: 16px;
              /* text-overflow，默认为clip。ellipsis文本溢出时自动加上省略号。 */
              text-overflow: ellipsis;
              /* 超出隐藏 */
              overflow: hidden;
              /* 文字不换行 */
              white-space: nowrap;
            }

            /* 消息时间 */
            .create-time {
              position: absolute;
              right: 20px;
              font-size: 12px;
            }
          }

          .content {
            /* 用户最近消息 */
            .message {
              max-width: 91%;
              font-size: 12px;
              /* text-overflow，默认为clip。ellipsis文本溢出时自动加上省略号。 */
              text-overflow: ellipsis;
              /* 超出隐藏 */
              overflow: hidden;
              /* 文字不换行 */
              white-space: nowrap;
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
        }
        /* 鼠标移入每个用户消息栏样式 */
        &:hover {
          color: #FFA500FF;
          background-color: #FFA5000A;
          cursor: pointer
        }
      }


    }
  }
}
</style>