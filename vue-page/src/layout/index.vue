<template>
  <div class="app-wrapper">
    <nav-bar :offlineUserOption="offlineUserOption"
             @getOfflineUserList="getOfflineUserList"/>

    <!-- 左侧菜单 -->
    <div class="aside">
      <ul class="nav">
        <li class="nav-item-top" @click="handlerNav(1)">
          <a class="nav-link" :style="navActivate === 'message' ? 'color: coral' : ''">
            <i class="el-icon-chat-line-round"></i>
            <span>消息</span>
          </a>
        </li>
        <li class="nav-item" @click="handlerNav(2)">
          <a class="nav-link" :style="navActivate === 'contacts' ? 'color: coral' : ''">
            <i class="el-icon-user"></i>
            <span>联系人</span>
          </a>
        </li>
        <li class="nav-item" @click="handlerNav(3)">
          <a class="nav-link" :style="navActivate === 'dynamics' ? 'color: coral' : ''">
            <i class="el-icon-chat-dot-round"></i>
            <span>动态</span>
          </a>
        </li>
      </ul>
    </div>

    <!-- 内容布局 -->
    <div class="chat-content">
      <!-- 消息列表 -->
      <div class="content-user-list" v-if="navActivate === 'message'">
        <div class="topping">
          <h3>最近</h3>
        </div>
        <!-- el-scrollbar 必须指定高度 -->
        <el-scrollbar class="user-scrollbar" ref="scrollbar" style="height: 900px">
          <ul class="user-message-list" style="overflow:auto">
            <li v-for="ele in messageUserList" v-bind:key="ele.id" @click="handlerMessageUser(ele.id)"
                :style="messageUserActivate === ele.id ? 'color: #FFA500FF; background-color: #FFA5002A;' : ''"
                class="user">
              <div class="user-avatar"></div>
              <span>{{ ele.name }}</span>
            </li>
          </ul>
        </el-scrollbar>
      </div>
      <!-- 联系人列表 -->
      <div class="content-user-list" v-if="navActivate === 'contacts'">
        <!-- el-scrollbar 必须指定高度 -->
        <el-scrollbar class="user-scrollbar" ref="scrollbar" style="height: 900px">
          <ul class="user-message-list" style="overflow:auto">
            <li v-for="ele in messageUserList" v-bind:key="ele.id" class="user">
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
            <div class="chat-object-avatar"></div>
            <div class="name">
              <p>{{ user.name }}</p>
              <span>{{ user.date }}</span>
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
          <div class="chatting-body-record">
            <!-- el-scrollbar 必须指定高度 -->
            <el-scrollbar ref="message-record-scrollbar" style="height: 800px">
              <ul>
                <!-- 1-发送;2-接收; -->
                <li :class="message.type === 1 ? 'send' :'receive'" v-for="message in messageList"
                    v-bind:key="message.id">
                  <div class="message-text">
                    {{ message.message }}
                    <div class="message-time">19:11</div>
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
              <el-input></el-input>
            </div>
            <!-- 发送按钮 -->
            <div class="box-send">
              <el-button icon="el-icon-position" type="primary"></el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>

import { mapGetters } from 'vuex';
import NavBar from '@/layout/components/navbar.vue';
import { getOfflineUserList } from '@/api/sys/user';

import('@/assets/styles/main.css');
import('@/assets/styles/aside.css');
import('@/assets/styles/content.css');
import('@/assets/styles/component.css');

export default {
  name: 'chat-vue',
  components: {
    NavBar,
  },
  props: {},
  data() {
    return {
      offlineUserOption: [],
      // 激活的导航菜单
      navActivate: 'message',
      // 激活的消息用户
      messageUserActivate: 1,
      user: {
        id: 1,
        name: 'Jack P. Angulo',
        date: '2023-12-11',
        remark: 'orem ipsum dolor sit amet consectetur',
      },
      // 最近消息用户列表
      messageUserList: [
        {
          id: 1,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 2,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 3,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 4,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 5,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 6,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 7,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 8,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 9,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 10,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 11,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 12,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 13,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
        {
          id: 14,
          name: 'Jack P. Angulo',
          remark: 'orem ipsum dolor sit amet consectetur',
        },
      ],
      // 消息列表
      messageList: [
        {
          id: 1,
          // 1-发送;2-接收;
          type: 1,
          message: '11111',
        },
        {
          id: 2,
          // 1-发送;2-接收;
          type: 2,
          message: '222222',
        },
        {
          id: 3,
          // 1-发送;2-接收;
          type: 1,
          message: '33333',
        },
        {
          id: 4,
          // 1-发送;2-接收;
          type: 2,
          message: '44444',
        },
        {
          id: 5,
          // 1-发送;2-接收;
          type: 1,
          message: '555555',
        },
        {
          id: 6,
          // 1-发送;2-接收;
          type: 2,
          message: '666666',
        },
        {
          id: 7,
          // 1-发送;2-接收;
          type: 1,
          message: '777777',
        },
        {
          id: 8,
          // 1-发送;2-接收;
          type: 1,
          message: '888888',
        },
        {
          id: 9,
          // 1-发送;2-接收;
          type: 1,
          message: '9999999',
        },
      ],
      websocket: undefined,
    };
  },
  computed: {
    ...mapGetters(['userInfo']),
  },
  watch: {},
  created() {
  },
  mounted() {
    // 消息记录页面加载后滚动到底部
    if (this.$refs['message-record-scrollbar']) {
      this.scrollDown();
    }
    this.getUserInfo();
    this.connect();
  },
  updated() {
    // 消息记录页面数据更新滚动到底部
    if (this.$refs['message-record-scrollbar']) {
      this.scrollDown();
    }
  },
  methods: {
    // 切换左侧菜单导航
    handlerNav(type) {
      if (type === 1) {
        this.navActivate = 'message';
      } else if (type === 2) {
        this.navActivate = 'contacts';
      } else {
        this.navActivate = 'dynamics';
      }
    },
    // 切换消息用户
    handlerMessageUser(id) {
      this.messageUserActivate = id;
      // 切换用户弹窗
    },
    // 获取用户信息
    async getUserInfo() {
      if (!this.userInfo) {
        await this.getOfflineUserList();
        // 从离线用户里面随机取一个用户
        if (this.offlineUserOption.length > 0) {
          const randomIndex = Math.floor(Math.random() * this.offlineUserOption.length);
          const user = this.offlineUserOption[randomIndex];
          this.$store.dispatch('user/setUserInfo', user);
        }
        // 排除自己
        this.offlineUserOption = this.offlineUserOption.filter(ele => ele.id !== this.userInfo.id);
      }
    },
    // 获取离线用户列表
    getOfflineUserList() {
      getOfflineUserList().then(res => {
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
    // 建立连接
    connect() {
      // 判断当前浏览器是否支持WebSocket
      if (!('WebSocket' in window)) {
        console.error('不支持WebSocket');
      }
      // websocket对象
      this.websocket = new WebSocket('ws://localhost/chat');
      //连接发生错误的回调方法
      this.websocket.onerror = function (e) {
        console.error('WebSocket连接发生错误', e);
      };
      // 连接成功建立的回调方法
      this.websocket.onopen = function () {
        console.log('WebSocket连接成功');
      };
      // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = function () {
        closeWebSocket();
      };

      // 关闭WebSocket连接
      function closeWebSocket() {
        this.websocket.close();
      }

      // 接收到消息的回调方法
      this.websocket.onmessage = function (event) {
        console.log('收到消息：', event.data);
      };
      // 连接关闭的回调方法
      this.websocket.onclose = function () {
        console.log('WebSocket连接关闭');
      };
    },
    //发送消息
    send(message) {
      this.websocket.send(message);
    },
    /**
     * 初始化聊天记录滚动条到底部
     */
    scrollDown() {
      this.$refs['message-record-scrollbar'].wrap.scrollTop = this.$refs['message-record-scrollbar'].wrap.scrollHeight;
    },
  },
};

</script>

<!-- html方式引入 -->
<!--<link rel="stylesheet" href="@/assets/styles/main.css">-->
<!--<link rel="stylesheet" href="@/assets/styles/navbar.css">-->
<!--<link rel="stylesheet" href="@/assets/styles/aside.css">-->
<!--<link rel="stylesheet" href="@/assets/styles/content.css">-->