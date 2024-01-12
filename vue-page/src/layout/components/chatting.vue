<template>
  <!-- 聊天框 -->
  <div class="content-chatting" v-if="this.recentlyMessageActivate">
    <div class="chatting-head">
      <!-- 聊天对象 -->
      <div class="chat-object">
        <div class="name">
          <p>{{ recentlyMessageActivate.name }}</p>
        </div>
      </div>
      <!-- 操作 -->
      <div class="chat-action">
        <div>
          <i class="el-icon-more"></i>
        </div>
      </div>
    </div>
    <div class="chatting-body">
      <!--  上滑加载 -->
      <div class="chatting-body-loading">
        {{ chattingLoading === 0 ? "1" : chattingLoading === 1 ? "上滑加载..." : "到底了" }}
      </div>
      <div class="chatting-body-record">
        <!-- el-scrollbar 必须指定高度 -->
        <el-scrollbar class="message-record-scrollbar" ref="messageRecordScrollbarRef">
          <ul>
            <!-- 1-发送;2-接收; -->
            <li class="message-record" :class="ele.send === 1 ? 'send' :'receive'"
                v-for="(ele,index) in (messageMap[recentlyMessageActivate.businessId])"
                v-bind:key="ele.id">
              <div
                  v-if="index > 0 && getTime(ele.sendTime, messageMap[recentlyMessageActivate.businessId][index - 1])"
                  class="message-time">
                {{ ele.time }}
              </div>
              <div class="message-content">
                <div class="message-header">
                  <img :src="ele.avatar" class="message-avatar" alt="">
                </div>
                <!-- 新消息提醒 -->
                <div class="message-tip">

                </div>
                <div class="message-text" v-html="ele.message"></div>
              </div>
            </li>
          </ul>
        </el-scrollbar>
      </div>
    </div>
    <!-- 聊天输入框 -->
    <div class="chatting-footer">
      <!-- 聊天输入框头部操作 -->
      <div class="footer-box-heard">
        <!-- 表情 -->
        <div class="box-expression action">
          <i class="el-icon-picture-outline-round"></i>
        </div>
        <!-- 文件 -->
        <div class="box-file action">
          <i class="el-icon-folder-opened"></i>
        </div>
        <!-- 文件 -->
        <!-- ... -->
      </div>
      <!-- 聊天输入框内容 -->
      <div>
        <el-input type="textarea"
                  resize='none'
                  :rows="7"
                  @keyup.enter.native="sendMessage"
                  v-model="content">

        </el-input>
      </div>
    </div>
  </div>
</template>

<script>

import {nextTick} from "vue";
import {currentDateTime, dateFormat} from "@/utils/date";
import {getMessageRecordByPaged} from "@/api/chat/messageRecord";
import {mapGetters} from "vuex";

export default {
  name: "chatting-container",
  components: {},
  props: {},
  data() {
    return {
      // 聊天内容
      content: "",
      // 0-初始化;1-上拉;2-无数据
      chattingLoading: 0,
      // 聊天消息分页条件
      pageDate: {
        size: 10,
        current: 1,
        total: 0,
        totalPage: 0
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo", "recentlyMessageMap", "recentlyMessageActivate", "messageMap"])
  },
  watch: {
    // recentlyMessageActivate: {
    //   handler() {
    //     this.getMessageRecordList();
    //   },
    //   deep: true
    // }
  },
  created() {
  },
  updated() {
  },
  mounted() {
    // 事件总线
    // bus.$on("getMessageRecordListBus", this.getMessageRecordList);
    // 必须是要页面加载完成以后
    this.$nextTick(() => {
      if (this.$refs.messageRecordScrollbarRef) {
        // 添加滚动条监听事件
        this.$refs.messageRecordScrollbarRef.wrap.addEventListener("scroll", this.handleScroll);
      }
      this.getMessageRecordList();
    });
  },
  methods: {
    handlerMessageUser() {
      this.pageDate = {size: 10, current: 1, total: 0, totalPage: 0};
      this.getMessageRecordList();
    },
    // 获取聊天消息列表
    async getMessageRecordList(push) {
      if (this.recentlyMessageActivate.type) {
        await getMessageRecordByPaged({
          businessId: this.recentlyMessageActivate.businessId,
          type: this.recentlyMessageActivate.type,
          ...this.pageDate
        }).then(res => {
          if (res.data.code === 200 && res.data.data) {
            const messageList = push ? this.messageMap[this.recentlyMessageActivate.businessId] : [];
            this.pageDate.total = res.data.data.total;
            this.pageDate.totalPage = Math.ceil(this.pageDate.total / this.pageDate.size);
            // 0-初始化;1-上拉;2-无数据
            this.chattingLoading = this.pageDate.totalPage > this.pageDate.current ? 1 : 2;
            res.data.data.rows.map(ele => {
              ele.time = dateFormat(ele.sendTime);
              messageList.unshift(ele);
            });
            this.$store.dispatch("message/setMessageMap", messageList);
          }
          // 消息记录页面加载后滚动到底部
          this.handlerMessageScrollDown();
        });
      }
    },
    // 发送消息
    sendMessage(e) {
      // 回车是13
      if (e.shiftKey && e.keyCode === 13) {
        console.log("1111");
        // 用户点击了shift+enter触发
        return;
      }
      this.content = this.content.replace(/\n+$/, "");
      console.log("content1:", JSON.stringify(this.content));
      // 用户点击了enter触发
      console.log("发送消息");
      let message = {
        message: this.content,
        receiveBusinessId: this.recentlyMessageActivate.businessId,
        businessId: this.recentlyMessageActivate.businessId,
        sendBusinessId: this.userInfo.id,
        name: this.userInfo.name,
        avatar: this.userInfo.avatar,
        type: this.recentlyMessageActivate.type,
        send: this.recentlyMessageActivate.businessId !== this.userInfo.id ? 1 : 2,
        sendTime: currentDateTime(),
        time: dateFormat(currentDateTime())
      };
      // 清空聊天框
      this.content = "";
      // 无法正确地检测到数据变化，无法自动重新渲染界面，使用 $forceUpdate方法强制更新组件确保界面上的数据和组件实例的数据保持一致
      // this.$forceUpdate();
      this.$emit("sendMessage", message);
      // 更新最近消息
      let recentlyMessage;
      if (this.recentlyMessageMap.has(message.businessId)) {
        recentlyMessage = this.recentlyMessageMap.get(message.businessId);
        recentlyMessage.time = message.time;
        recentlyMessage.sendTime = message.sendTime;
        recentlyMessage.message = message.message;
      } else {
        recentlyMessage = {
          ...this.recentlyMessageActivate,
          message: message.time,
          sendTime: message.sendTime,
          time: message.message
        };
      }
      this.$store.dispatch("message/setRecentlyMessage", recentlyMessage);
      this.$store.dispatch("message/pushMessageMap", message);
      // 发送消息滑动到最底部
      this.handlerMessageScrollDown();
    },
    // 获取上一条消息和当前消息发送时间是否相差60秒
    getTime(sendTime, previousSendTime) {
      return (new Date(sendTime).getTime() - new Date(previousSendTime.sendTime).getTime()) / 1000 > 60;
    },
    // 初始化聊天记录滚动条到底部
    handlerMessageScrollDown() {
      nextTick(() => {
        if (this.$refs["messageRecordScrollbarRef"]) {
          this.$refs["messageRecordScrollbarRef"].wrap.scrollTop = this.$refs["messageRecordScrollbarRef"].wrap.scrollHeight;
        }
      });
    },
    // 聊天记录滚动事件
    async handleScroll(event) {
      // 如果滑动到了距离顶部50，并且还有下一页数据，0-初始化;1-上拉;2-无数据
      // 这里计算滑动获取历史消息记录后距离底部的位置
      const bottom = event.target.scrollHeight - event.target.scrollTop - event.target.clientHeight;
      if (event.target.scrollTop === 0 && this.pageDate.current + 1 <= this.pageDate.totalPage) {
        this.pageDate.current = this.pageDate.current + 1;
        // 这里要同步调用，不然数据还没加载，滚动条高度获取不对，用nextTick后高度也不对
        await this.getMessageRecordList(true);
        // 定位到滑动加载数据的位置
        this.$refs["messageRecordScrollbarRef"].wrap.scrollTop = event.target.scrollHeight - bottom - event.target.clientHeight;
      }
    }
  }
};
</script>

<style scoped lang='scss'>
/* 聊天框 */
.content-chatting {
  display: flex;
  flex: 1;
  /* 设置主轴的方向 */
  flex-direction: column;

  /* 聊天框顶部用户头像相关信息 */
  .chatting-head {
    height: 70px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem;
    border-bottom: 1px solid #70809054 !important;
    /* box-shadow: 水平阴影 垂直阴影 模糊距离 阴影尺寸 阴影颜色 内外阴影; */
    box-shadow: 0 1px 3px rgba(0, 0, 0, .075) !important;

    /* 聊天对象 */
    .chat-object {
      display: flex;

      /* 对象名称 */
      .name {
        font-size: 18px;
      }
    }

    /* 右侧操作按钮 */
    .chat-action {
      display: flex;
      align-items: center;

      i {
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        width: 50px;
        font-size: 20px;
      }
    }

  }

  /* 上滑加载 */
  .chatting-body {

    .chatting-body-loading {
      margin-top: 10px;
      display: flex;
      justify-content: center;
    }

    .chatting-body-record {
      .message-record-scrollbar {
        height: 650px;
      }

      .message-record:nth-last-child(1) {
        margin-bottom: 30px;
      }

      .message-record {
        margin: 10px 0;
        /* 消息日期 */
        .message-time {
          display: flex;
          justify-content: center;
          margin: 20px 0;
          color: rgba(222, 184, 135, 0.92);
        }

        /* 消息内容 */
        .message-content {
          display: flex;

          .message-header {
            padding: 0 10px;
            /* 消息头像 */
            .message-avatar {
              width: 40px !important;
              height: 40px !important;
              border-radius: 50%;
            }

            .send .message-avatar {

            }

            .receive .message-avatar {

            }
          }

          /* 消息文字样式 */
          .message-text {
            padding: 10px;
            /* 设置文字的强制自动换行，但只对英文起作用，以字母作为换行依据。 */
            word-break: break-all;
            /* 设置文字的强制自动换行，但只对英文起作用，以单词作为换行依据。 */
            word-wrap: break-word;
            /* 动态计算盒子的宽度 */
            max-width: 45%;
            border-radius: 8px;
            // 解析换行符
            white-space: pre-wrap;
          }
        }
      }

      /* 发送的消息样式，布局在最右侧 */
      .send {
        .message-content {
          // justify-content: flex-end;
          /* flex-flow属性是flex-direction属性和flex-wrap属性的简写形式，默认值为row nowrap，让发送头像排列在最后 */
          flex-flow: row-reverse;

          .message-text {
            background-color: #FFC0CB54;
          }

          .message-text::after {
            border-top: 5px solid transparent;
            border-left: 5px solid #FFC0CB54;
            border-bottom: 5px solid transparent;
            content: '';
            position: absolute;
            right: -5px;
          }
        }
      }

      /* 接收的消息样式 */
      .receive {
        .message-content {
          /* 接收消息背景色 */
          .message-text {
            background-color: #7FFFD43D;
          }

          .message-text::after {
            border-top: 5px solid transparent;
            border-right: 5px solid #7FFFD43D;
            border-bottom: 5px solid transparent;
            content: '';
            position: absolute;
            left: -5px;
          }
        }
      }
    }
  }

  /* 聊天输入框相关样式 */
  .chatting-footer {
    background-color: #F0F8FF1D;
    /* 输入框 */
    border-top: 1px solid #70809054;

    .footer-box-heard {
      display: flex;

      .action {
        padding: 10px;
        margin: 10px 0 10px 10px;
      }

      // 鼠标
      .action:hover {
        cursor: pointer;
        background-color: rgba(217, 217, 217, 0.25);
        border-radius: 50%;
      }

      i {
        font-size: 25px;
      }
    }

    // 把边框去掉
    ::v-deep .el-textarea__inner {
      border: none;
    }
  }
}
</style>