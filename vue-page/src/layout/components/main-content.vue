<template>
  <!-- 内容布局 -->
  <div class="main-content-container">
    <!-- 消息列表 -->
    <div class="content-user-list" v-if="navActivate === 'message'">
      <div class="topping">
        <h3>最近</h3>
      </div>
      <!-- el-scrollbar 必须指定高度 -->
      <el-scrollbar class="user-scrollbar">
        <ul class="user-message-list" v-infinite-scroll="handleUserMessageScrollBottom">
          <li v-for="ele in messageUserMap" v-bind:key="ele.id" @click="handlerMessageUser(ele)"
              :style="messageUserActivate.id === ele.id ? 'color: #FFA500FF; background-color: #FFA5002A;' : ''"
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
    <!-- 联系人列表 -->
    <div class="content-user-list" v-if="navActivate === 'contacts'">
      <!-- el-scrollbar 必须指定高度，溢出滚动(overflow-y : auto)，才会触发load事件 -->
      <el-scrollbar class="user-scrollbar">
        <ul class="user-message-list">
          <li v-for="ele in messageUserMap" v-bind:key="ele.id" class="user">
            <div class="user-avatar"></div>
            <span>{{ ele.name }}</span>
          </li>
        </ul>
      </el-scrollbar>
    </div>
    <!-- 聊天框 -->
    <div class="content-chatting">
      <div class="chatting-head">
        <!-- 聊天对象 -->
        <div class="chat-object">
          <img :src="messageUserActivate.avatar" class="chat-object-avatar" alt="">
          <div class="name">
            <p>{{ messageUserActivate.name }}</p>
            <span>{{ messageUserActivate.date }}</span>
          </div>
        </div>
        <!-- 操作 -->
        <div class="chat-action">
          <div>
            <i class="el-icon-chat-square"></i>
          </div>
          <div>
            <i class="el-icon-more"></i>
          </div>
        </div>
      </div>
      <div class="chatting-body">
        <!--  上滑加载 -->
        <div class="chatting-body-loading">
          {{ chattingLoading === 0 ? "" : chattingLoading === 1 ? "上滑加载..." : "到底了" }}
        </div>
        <div class="chatting-body-record">
          <!-- el-scrollbar 必须指定高度 -->
          <el-scrollbar class="message-record-scrollbar" ref="messageRecordScrollbarRef">
            <ul>
              <!-- 1-发送;2-接收; -->
              <li :class="ele.send === 1 ? 'send' :'receive'"
                  class="message-record"
                  v-for="ele in this.messageMap[messageUserActivate.sendBusinessId]"
                  v-bind:key="ele.id">
                <img :src="ele.avatar" class="message-avatar"
                     :class="ele.send === 1 ? 'message-avatar-send' :'message-avatar-receive'" alt="">
                <div class="message-text">
                  {{ ele.message }}
                  <div class="message-time">{{ ele.time }}</div>
                </div>
              </li>
            </ul>
          </el-scrollbar>
        </div>
      </div>
      <!-- 聊天输入框 -->
      <div class="chatting-footer">
        <div class="footer-box">
          <!-- 表情 -->
          <div class="box-expression">
            <i class="el-icon-picture-outline-round"></i>
          </div>
          <!-- 输入框 -->
          <div class="box-input">
            <el-input @keyup.enter.native="sendMessage" v-model="content"></el-input>
          </div>
          <!-- 发送按钮 -->
          <div class="box-send">
            <el-button icon="el-icon-position" @click="sendMessage"
                       type="primary"></el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import {getMessageList, getRecentlyMessageList} from "@/api/sys/message";
import {mapGetters} from "vuex";
import {nextTick} from "vue";
import {currentDateTime, dateFormat} from "@/utils/date";

export default {
  name: "main-content-container",
  components: {},
  props: {
    navActivate: {
      type: String
    }
  },
  data() {
    return {
      // 聊天内容
      content: "",
      // 激活的消息用户
      messageUserActivate: {},
      // 最近消息用户列表
      messageUserMap: {
        // id: {messageUser}
      },
      // 用户聊天消息列表
      messageMap: {
        // id:[message]
      },
      lastPositionY: null, // 记录上次滚动条位置
      // 0-初始化;1-上拉;2-无数据
      chattingLoading: 0,
      pageDate: {
        size: 10,
        current: 1,
        total: 0,
        totalPage: 0
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo"])
  },
  watch: {},
  created() {
  },
  mounted() {
    // 获取最近消息列表
    if (this.userInfo) {
      this.getRecentlyMessageList();
    }
    // 必须是要页面加载完成以后
    this.$nextTick(() => {
      // 需求：只有有的时候需要加 返回顶部按钮
      if (this.$refs.messageRecordScrollbarRef) {
        // 监听滚动条
        this.$refs.messageRecordScrollbarRef.wrap.addEventListener("scroll", this.handleScroll);
      }
    });
  },
  updated() {
    // 消息记录页面数据更新滚动到底部
    if (this.$refs["messageRecordScrollbarRef"]) {
      // this.scrollDown();
    }
    if (this.messageMap) {
      console.log("this.messageMap:", this.messageMap);
    }
    if (this.messageUserMap) {
      console.log("messageUserMap:", this.messageUserMap);
    }
    if (this.messageUserActivate) {
      console.log("messageUserActivate:", this.messageUserActivate);
    }
  },
  methods: {
    // 切换消息用户
    getRecentlyMessageList() {
      getRecentlyMessageList().then(res => {
        if (res.data.code === 200 && res.data.data) {
          res.data.data.map(ele => {
            ele.time = dateFormat(ele.createTime);
            this.messageUserMap[ele.sendBusinessId] = ele;
          });
          const keys = Object.keys(this.messageUserMap);
          if (!this.messageUserActivate.id && this.messageUserMap && keys.length > 0) {
            this.messageUserActivate = this.messageUserMap[keys[0]];
            this.getMessageList();
          }
        }
      });
    },
    // 获取最近消息列表
    handlerMessageUser(messageUser) {
      if (this.messageUserActivate.id !== messageUser.id) {
        this.messageUserActivate = messageUser;
        this.pageDate = {size: 10, current: 1, total: 0, totalPage: 0};
        this.getMessageList();
      }
    },
    // 获取聊天消息列表
    getMessageList() {
      getMessageList({
        sendBusinessId: this.messageUserActivate.sendBusinessId,
        type: this.messageUserActivate.type,
        ...this.pageDate
      }).then(res => {
        if (res.data.code === 200 && res.data.data) {
          const messageList = [];
          this.pageDate.total = res.data.data.total;
          this.pageDate.totalPage = Math.ceil(this.pageDate.total / this.pageDate.size);
          res.data.data.rows.map(ele => {
            ele.time = dateFormat(ele.createTime);
            messageList.unshift(ele);
          });
          if (res.data.data.rows.length > 0) {
            this.$set(this.messageMap, this.messageUserActivate.sendBusinessId, messageList);
          }
        }
        // 消息记录页面加载后滚动到底部
        this.handlerMessageScrollDown();

      });
    },
    // 最近消息滚动到底触发事件
    handleUserMessageScrollBottom() {
      console.log("111");
    },
    // 发送消息
    sendMessage() {
      let message = {
        message: this.content,
        receiveBusinessId: this.messageUserActivate.sendBusinessId,
        sendBusinessId: this.userInfo.id,
        type: this.messageUserActivate.type,
        send: this.messageUserActivate.sendBusinessId !== this.userInfo.id ? 1 : 2,
        createTime: currentDateTime(),
        time: dateFormat(currentDateTime())
      };
      this.messageMap[this.messageUserActivate.sendBusinessId].push(message);
      this.$emit("sendMessage", message);
      // 清空聊天框
      this.content = "";
      // 发送消息滑动到最底部
      this.handlerMessageScrollDown();
      // 把聊天用户放到第一位
    },
    // 添加收到的消息到聊天消息列表
    addMessage(data) {
      // 如果收到的消息是当前聊天用户，把消息添加到聊天对话中
      let messageUser = {
        id: data.id,
        sendBusinessId: data.sendBusinessId,
        receiveBusinessId: data.receiveBusinessId,
        type: data.type,
        message: data.message,
        createTime: data.createTime,
        time: data.time,
        send: data.receiveBusinessId !== this.userInfo.id ? 1 : 2
      };
      if (this.messageUserActivate.sendBusinessId === data.sendBusinessId) {
        this.messageMap[data.sendBusinessId].push(messageUser);
      } else {
        // 否则把消息更新到最近聊天用户的消息记录上
        // 无法正确地检测到数据变化，无法自动重新渲染界面，使用 $forceUpdate方法强制更新组件确保界面上的数据和组件实例的数据保持一致
        this.$set(this.messageUserMap[data.sendBusinessId], "message", data.message);
        this.$set(this.messageUserMap[data.sendBusinessId], "createTime", data.createTime);
        this.$set(this.messageUserMap[data.sendBusinessId], "time", data.time);
        this.$forceUpdate();
      }
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
    handleScroll(event) {
      // const currentPosition = event.target.scrollTop; // 获取当前滚动条位置
      // if (this.lastPositionY === null || this.lastPositionY > currentPosition) {
      //   console.log("向上滚动");
      // } else if (this.lastPositionY < currentPosition) {
      //   console.log("向下滚动");
      // }
      // this.lastPositionY = currentPosition; // 更新上次滚动条位置
      // console.log("currentPosition:", currentPosition);
      // 如果滑动到了距离顶部50，并且还有下一页数据，0-初始化;1-上拉;2-无数据
      this.chattingLoading = event.target.scrollTop < 50 ? (this.pageDate.current + 1 <= this.pageDate.totalPage ? 1 : 2) : 0;
      if (event.target.scrollTop === 0 && this.pageDate.current + 1 <= this.pageDate.totalPage) {
        this.pageDate.current = this.pageDate.current + 1;
        getMessageList({
          sendBusinessId: this.messageUserActivate.sendBusinessId,
          type: this.messageUserActivate.type,
          ...this.pageDate
        }).then(res => {
          if (res.data.code === 200 && res.data.data) {
            res.data.data.rows.map(ele => {
              console.log("ele.sendBusinessId:", ele.sendBusinessId);
              console.log("this.messageMap[ele.sendBusinessId]:", this.messageMap[ele.sendBusinessId]);
              if (this.messageMap[ele.sendBusinessId]) {
                this.messageMap[ele.sendBusinessId].unshift(ele);
              }
            });
            nextTick(() => {
              // 这里计算滑动获取历史消息记录后的位置
              this.$refs["messageRecordScrollbarRef"].wrap.scrollTop = (event.target.scrollHeight / 3);
            });
          }
        });
      }
    }
  }
};
</script>

<style>
/* 内容样式 */
.main-content-container {
  display: flex;
  flex: 1 1 auto;
}

/* 滚动条 */
.main-content-container .user-scrollbar {
  overflow-y: auto;
  /* 动态计算盒子的高度 */
  height: calc(100% - 64px);
  border-right: 1px solid #70809054 !important;
  /* box-shadow: 水平阴影 垂直阴影 模糊距离 阴影尺寸 阴影颜色 内外阴影; */
  box-shadow: 1px 0 1px rgba(0, 0, 0, .075) !important;
}

/* 最近聊天 */
.main-content-container .content-user-list .topping {
  padding: 20px;
  /* border-bottom: 1 px solid #70809054 !important; */
  border-right: 1px solid #70809054 !important;
  /* box-shadow: 水平阴影 垂直阴影 模糊距离 阴影尺寸 阴影颜色 内外阴影; */
  /* box-shadow: 1 px 0 1 px rgba(0, 0, 0, .075) !important; */
}

/* 用户消息列表 */
.main-content-container .content-user-list {
  width: 400px;
}

.main-content-container .content-user-list .user-message-list {
  display: flex;
  /* 设置主轴的方向 */
  flex-direction: column;
}

/* 每个用户消息样式 */
.main-content-container .content-user-list .user-message-list .user-message {
  position: relative;
  display: flex;
  align-items: center;
  padding: 20px;
  height: 80px;
  border-bottom: 1px solid #70809054 !important;
}

/* 用户头像 */
.main-content-container .content-user-list .user-message-list .user-message .avatar {
  margin-right: 20px;
  display: block;
  width: 40px !important;
  height: 40px !important;
  border-radius: 50%;
}

/* 用户名称 */
.main-content-container .content-user-list .user-message-list .user-message .name {
  font-size: 16px;
}

/* 用户最近消息 */
.main-content-container .content-user-list .user-message-list .user-message .message {
  margin-top: 5px;
  font-size: 12px;
}

/* 消息时间 */
.main-content-container .content-user-list .user-message-list .user-message .create-time {
  position: absolute;
  right: 20px;
  font-size: 12px;
}

/* 消息数量 */
.main-content-container .content-user-list .user-message-list .user-message .tip {
  position: absolute;
  right: 20px;
  bottom: 20px;
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

/* 鼠标移入每个用户消息栏样式 */
.main-content-container .content-user-list .user-message-list .user-message:hover {
  color: #FFA500FF;
  background-color: #FFA5000A;
  cursor: pointer
}

/* 聊天框 */
.main-content-container .content-chatting {
  display: flex;
  flex: 1;
  /* 设置主轴的方向 */
  flex-direction: column;
}

/* 聊天框顶部用户头像相关信息 */
.main-content-container .content-chatting .chatting-head {
  height: 70px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid #70809054 !important;
  /* box-shadow: 水平阴影 垂直阴影 模糊距离 阴影尺寸 阴影颜色 内外阴影; */
  box-shadow: 0 1px 3px rgba(0, 0, 0, .075) !important;
}

/* 聊天对象 */
.main-content-container .content-chatting .chatting-head .chat-object {
  display: flex;
}

/* 对象名称 */
.main-content-container .content-chatting .chatting-head .chat-object .name {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

/* 对象头像 */
.main-content-container .content-chatting .chatting-head .chat-object .chat-object-avatar {
  margin-right: 20px;
  display: block;
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

/* 右侧操作按钮 */
.main-content-container .content-chatting .chatting-head .chat-action {
  display: flex;
  align-items: center;
}

.main-content-container .content-chatting .chatting-head .chat-action i {
  padding: 0 .5rem;
  border-radius: 100px;
  height: 35px;
  width: 35px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

/* 上滑加载 */
.main-content-container .content-chatting .chatting-body .chatting-body-loading {
  position: fixed;
  right: 400px;
  top: 150px;
}

.main-content-container .content-chatting .chatting-body .chatting-body-record .message-record-scrollbar {
  height: 800px;
}

/* 发送的消息样式，布局在最右侧 */
.main-content-container .content-chatting .chatting-body .chatting-body-record .send {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

/* 接收的消息样式 */
.main-content-container .content-chatting .chatting-body .chatting-body-record .receive {

}

/* 消息头像 */
.main-content-container .content-chatting .chatting-body .chatting-body-record .message-avatar {
  width: 40px !important;
  height: 40px !important;
  border-radius: 50%;
}

.main-content-container .content-chatting .chatting-body .chatting-body-record .message-avatar-send {

}

.main-content-container .content-chatting .chatting-body .chatting-body-record .message-avatar-receive {

}

/* 消息文字样式 */
.main-content-container .content-chatting .chatting-body .chatting-body-record .message-text {
  display: flex;
  margin: 20px;
  padding: 20px;
  max-width: 200px;
  border-radius: 50px;
}

/* 消息日期 */
.main-content-container .content-chatting .chatting-body .chatting-body-record .message-time {
  position: relative;
  top: 2px;
  margin-left: 10px;
  color: rgba(222, 184, 135, 0.92);
}

/* 发送消息背景色 */
.main-content-container .content-chatting .chatting-body .chatting-body-record .send .message-text {
  background-color: #FFC0CB54;
}

/* 接收消息背景色 */
.main-content-container .content-chatting .chatting-body .chatting-body-record .receive .message-text {
  background-color: #7FFFD43D;
}

/* 聊天输入框相关样式 */
.main-content-container .content-chatting .chatting-footer {
  background-color: #F0F8FF1D;
  height: 100px;
}

.main-content-container .content-chatting .chatting-footer .footer-box {
  display: flex;
  justify-content: space-between;
  padding: 30px;
}

/* 表情 */
.main-content-container .content-chatting .chatting-footer .footer-box .box-expression {
  display: flex;
  align-items: center;
  font-size: 30px;
}

/* 输入框 */
.main-content-container .content-chatting .chatting-footer .footer-box .box-input {
  flex: 1 1 auto;
  position: relative;
  padding-left: 30px;
  margin-right: 30px;
}

/* 发送 */
.main-content-container .content-chatting .chatting-footer .footer-box .box-send {
  display: flex;
  align-items: center;
}
</style>