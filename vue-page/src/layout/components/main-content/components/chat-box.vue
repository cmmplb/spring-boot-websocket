<template>
  <!-- 聊天框 -->
  <div class="chat-box-content" v-if="recentlyMessageActivate">
    <div class="chat-box-head flex bdb">
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
    <div class="chat-box-body">
      <!--  上滑加载 -->
      <div class="chat-box-body-loading">
        {{ chatLoading === 0 ? "" : chatLoading === 1 ? "上滑加载..." : "" }}
      </div>
      <div class="chat-box-body-record">
        <!-- el-scrollbar 必须指定高度 -->
        <el-scrollbar class="message-record-scrollbar" ref="messageRecordScrollbarRef">
          <ul>
            <!-- 1-发送;2-接收; -->
            <li class="message-record" :class="ele.send === 1 ? 'send' :'receive'"
                v-for="(ele,index) in messageMap.get(recentlyMessageActivate.businessId)"
                v-bind:key="index">
              <div
                  v-if="index > 0 && getTime(ele.sendTime, messageMap.get(recentlyMessageActivate.businessId)[index - 1])"
                  class="message-time">
                {{ ele.time }}
              </div>
              <div class="message-content">
                <div class="message-header">
                  <img :src="ele.avatar" class="message-avatar" alt="">
                </div>
                <!-- todo:新消息提醒 -->
                <div class="message-tip">

                </div>
                <div class="message-text" v-html="ele.message">

                </div>
                <el-tooltip effect="dark" :content="ele.status === 0 ? '未读' : '已读'" placement="top">
                  <div class="message-read" v-if="ele.send === 1">
                    <i v-if="ele.status === 0" class="icon-weidu unread"></i>
                    <i v-if="ele.status === 1" class="icon-ic_yidu read"></i>
                  </div>
                </el-tooltip>
              </div>
            </li>
          </ul>
        </el-scrollbar>
      </div>
    </div>
    <!-- 聊天输入框 -->
    <div class="chat-box-footer">
      <!-- 聊天输入框头部操作 -->
      <div class="footer-box-heard">
        <EmojiDialog id="emojiDialog" v-if="showEmoji" @handlerEmojiClick="handlerEmojiClick"/>
        <!-- 表情 -->
        <i id="box-emoji" class="icon-biaoqing box-emoji action" @click="handlerShowEmoji"></i>
        <!-- 文件 -->
        <i class="icon-weibiaoti-- box-file action"></i>
        <!-- 文件 -->
        <!-- ... -->
      </div>
      <!-- 聊天输入框内容，让div成为可编辑状态contenteditable="true" -->
      <div class="footer-box-content">
        <div @click="clickInput"
             @paste="pasteEvent"
             @keydown="keydownEvent"
             class="input-box"
             ref="inputBox"
             contenteditable="true"
        ></div>
      </div>
    </div>
  </div>
</template>

<script>

import {currentDateTime, dateFormat} from "@/utils/date";
import {getByPaged, read} from "@/api/chat/messageRecord";
import {mapGetters} from "vuex";
import websocket from "@/utils/websocket";
import EmojiDialog from "@/components/emoji-dialog.vue";
import {echoEmojis, parseToText} from "@/utils/emoji";
import {uuidTrim} from "@/utils/util";

export default {
  name: "chat-box-container",
  components: {
    EmojiDialog
  },
  props: {},
  data() {
    return {
      // 聊天内容
      content: "",
      // 0-初始化;1-上拉;2-无数据
      chatLoading: 0,
      // 分页条件
      pageDate: {
        size: 10,
        current: 1,
        total: 0,
        totalPage: 0
      },
      showEmoji: false
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
    // 点击添加表情框以外区域事件
    document.addEventListener("mouseup", (e) => {
      const emojiDialog = document.getElementById("emojiDialog");
      const boxEmoji = document.getElementById("box-emoji");
      if (emojiDialog) {
        if (!emojiDialog.contains(e.target) && !boxEmoji.contains(e.target)) {
          this.handlerCloseEmoji();
        }
      }
    });
    // 事件总线
    // bus.$on("getMessageRecordListBus", this.getMessageRecordList);
    // 必须是要页面加载完成以后
    this.$nextTick(() => {
      if (this.$refs.messageRecordScrollbarRef) {
        // 添加滚动条监听事件
        console.log("添加滚动条监听事件");
        this.$refs.messageRecordScrollbarRef.wrap.addEventListener("scroll", this.handleScroll);
      }
      this.getMessageRecordList();
      window.onMessage = this.onMessage;
      window.onReadMessage = this.onReadMessage;
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
        await getByPaged({
          businessId: this.recentlyMessageActivate.businessId,
          type: this.recentlyMessageActivate.type,
          ...this.pageDate
        }).then(res => {
          if (res.data.code === 200 && res.data.data) {
            const messageList = push ? this.messageMap.get(this.recentlyMessageActivate.businessId) : [];
            this.pageDate.total = res.data.data.total;
            this.pageDate.totalPage = Math.ceil(this.pageDate.total / this.pageDate.size);
            // 0-初始化;1-上拉;2-无数据
            this.chatLoading = this.pageDate.totalPage > this.pageDate.current ? 1 : 2;
            if (res.data.data.total > 0) {
              res.data.data.rows.map(ele => {
                ele.time = dateFormat(ele.sendTime, 2);
                ele.message = echoEmojis(ele.message);
                messageList.unshift(ele);
              });
            }
            this.$store.dispatch("message/setMessageMap", messageList);
          } else {
            this.$message.error(res.data.msg);
          }
          // 消息记录页面加载后滚动到底部
          this.handlerMessageScrollDown();
        });
      }
    },
    // 显示表情框
    handlerShowEmoji() {
      console.log("this.showEmoji:", this.showEmoji);
      this.showEmoji = !this.showEmoji;
      console.log("this.showEmoji:", this.showEmoji);
    },
    // 关闭表情框
    handlerCloseEmoji() {
      this.showEmoji = false;
    },
    // 聊天框表情，https://blog.csdn.net/weixin_52707625/article/details/131861061
    handlerEmojiClick(ele) {
      const selection = window.getSelection(); // 获取光标
      // 如果没有焦点或者焦点不在输入框内，聚焦到输入框
      if (selection.anchorNode !== this.$refs.inputBox && !this.$refs.inputBox.contains(selection.anchorNode)) {
        // 聚焦到输入框
        selection.removeAllRanges();
        this.$refs.inputBox.focus();
        if (this.$refs.inputBox.lastChild) {
          selection.getRangeAt(0).setStartAfter(this.$refs.inputBox.lastChild);
        }
        selection.collapseToEnd();
      }
      // 创建节点
      let img = document.createElement("img");
      img.src = ele.src;
      img.className = "in-img";
      img.setAttribute("name", ele.regName);

      let range = selection.getRangeAt(0);
      range.deleteContents();

      // 在 Range 的起点处插入节点。
      range.insertNode(img);
      range = range.cloneRange();
      range.setStartAfter(img);
      // 向指定端点折叠该 Range
      range.collapse();
      selection.removeAllRanges();
      selection.addRange(range);
      // this.inputHandle();
    },
    // 实现去格式粘贴=>http://www.kt5.cn/fe/2020/05/20/contenteditable-true-format-paste/
    pasteEvent(event) {
      console.log("pasteEvent");
      let e = event || window.event;
      e.preventDefault();
      e = e.originalEvent || e;
      const text = e.clipboardData ? e.clipboardData.getData("text/plain") : prompt("请在这里粘贴文本", "");
      if (e.clipboardData) { // 支持直接 clipboardData，则直接 OK
        document.execCommand("insertText", false, text);
      } else { // 不支持则取用户在 prompt 提示输入框粘贴的内容（自动去格式的）
        let sel, textRange;
        try {
          document.execCommand("ms-beginUndoUnit", false, null); // 开始给 IE 增加撤回功能
        } catch (e) {
          console.log("e:", e);
        }
        if (document.selection) { // IE9以下支持：document.selection
          textRange = document.selection.createRange();
          textRange.text = text;
          textRange.collapse(false); // 取消选择
          textRange.select(); // 重新选择
        } else if (window.getSelection) { // IE9以上、Firefox、Safari、Chrome和Opera支持：window.getSelection()
          sel = window.getSelection();
          const range = sel.getRangeAt(0);
          range.deleteContents(); // 删除 range 的内容
          const textNode = document.createTextNode(text);
          range.insertNode(textNode); // 向 range 插入 text 节点
          range.selectNodeContents(textNode); // 选中 text 节点
          sel.removeAllRanges(); // 删除选区中所有的 range
          sel.addRange(range); // 向选区增加刚才处理后的 range（只含 text 节点）
        }
        try {
          document.execCommand("ms-endUndoUnit", false, null); // 结束给 IE 增加撤回功能
        } catch (e) {
          console.log("e:", e);
        }
      }
    },
    keydownEvent(e) {
      if (e.shiftKey && e.keyCode === 13) {
        // ctrl+enter
        console.log("shift+enter触发换行,this.$refs.inputBox.innerHTML:", this.$refs.inputBox.innerHTML);
      } else if (e.keyCode === 13) {
        let html = this.$refs.inputBox.innerHTML;
        let res = parseToText(html);
        // var text = this.parseDefaultTab(this.$refs.inputBox.innerHTML);
        this.content = res.text;
        // 发送消息
        this.sendMessage();
        e.preventDefault(); // 阻止浏览器默认换行操作
        return false;
      }
    },
    // 点击输入框内的图标时，移动光标到对应位置
    clickInput(e) {
      // 如果点击的不是HTML标签，无需处理
      if (!(e.target instanceof HTMLImageElement)) {
        return;
      }
      let target = e.target;
      // 获取点击图片的中心位置
      let targetX = target.x + Math.floor(target.width / 2);

      // Selection对象表示用户选择的文本范围或插入符号的当前位置。它代表页面中的文本选区，可能横跨多个元素。文本选区由用户拖拽鼠标经过文字而产生。
      // https://www.jianshu.com/p/5997a90aab64
      // 获取光标
      let selection = window.getSelection();
      // 通常情况下我不会直接操作 selection 对象，而是需要操作用seleciton对象所对应的用户选择的ranges(区域)，俗称”拖蓝“。
      let range = selection.getRangeAt(0);

      // 返回拥有和原 Range 相同端点的克隆 Range 对象
      range = range.cloneRange();
      // 调用setStart()和setEnd()方法，来修改一个光标的位置或拖蓝范围。这两个方法接受的参数为各自的起终节点和偏移量。
      range.setStartBefore(target);
      if (e.x < targetX) {
        range.setEndBefore(target);
      } else {
        range.setEndAfter(target);
      }
      // 向指定端点折叠该 Range
      range.collapse();
      // 删除所有range对象。
      selection.removeAllRanges();
      // 向当前选取添加一个range对象
      selection.addRange(range);
    },
    // 发送消息
    async sendMessage() {
      if (!this.content) {
        return;
      }
      const businessId = Number.parseInt(this.recentlyMessageActivate.businessId.replace(this.recentlyMessageActivate.type + "-", ""));
      let message = {
        message: this.content,
        receiveBusinessId: businessId,
        businessId: this.recentlyMessageActivate.businessId,
        sendBusinessId: this.userInfo.id,
        name: this.userInfo.name,
        avatar: this.userInfo.avatar,
        type: this.recentlyMessageActivate.type,
        status: 0,
        uuid: uuidTrim(),
        send: businessId !== this.userInfo.id ? 1 : 2,
        sendTime: currentDateTime(),
        recentlyTime: dateFormat(currentDateTime(), 3),
        time: dateFormat(currentDateTime(), 2)
      };
      console.log("message:", message);
      // 更新最近消息
      let recentlyMessage;
      if (this.recentlyMessageMap.has(message.businessId)) {
        recentlyMessage = this.recentlyMessageMap.get(message.businessId);
        recentlyMessage.time = message.time;
        recentlyMessage.recentlyTime = message.recentlyTime;
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
      await this.$store.dispatch("message/setRecentlyMessage", recentlyMessage);
      // 添加消息到聊天记录
      await this.$store.dispatch("message/pushMessageMap", {
        ...message, message: echoEmojis(this.content)
      });
      // 清空聊天框
      this.$refs.inputBox.innerHTML = "";
      // 无法正确地检测到数据变化，无法自动重新渲染界面，使用 $forceUpdate方法强制更新组件确保界面上的数据和组件实例的数据保持一致
      // this.$forceUpdate();
      console.log("发送消息：messageMap:", this.messageMap);
      websocket.send(message);
      // 发送消息滑动到最底部
      this.handlerMessageScrollDown();
    },
    // 收到消息
    async onMessage(data) {
      console.log("收到消息:", data);
      data.send = data.receiveBusinessId !== this.userInfo.id ? 1 : 2;
      data.businessId = data.type + "-" + data.sendBusinessId;
      // 把消息更新到最近聊天用户的消息记录上
      await this.$store.dispatch("message/setRecentlyMessage", data);
      // 如果收到的消息不是是当前聊天用户，设置消息未读数量
      if (this.recentlyMessageActivate.businessId !== data.businessId) {
        await this.$store.dispatch("message/setMessageUnRead", data.businessId);
      } else {
        // 如果收到的消息是当前聊天用户，把消息添加到聊天对话中
        // 消息记录中表情包回显
        await this.$store.dispatch("message/pushMessageMap", {...data, message: echoEmojis(data.message)});
        // 调用后台标记消息已读
        await read(data.uuid);
        console.log("调用后台标记消息已读");
      }
      // this.$forceUpdate();
      // 消息滑动到最底部
      this.handlerMessageScrollDown();
    },
    // 消息已读
    onReadMessage(data) {
      console.log("消息已读：", data);
      // 更新单条消息状态
      if (data.type === 3) {
        this.$store.dispatch("message/setMessageRecordRead", data.message);
      } else {
        // 更新所有消息状态
        this.$store.dispatch("message/setAllMessageRecordRead", data.message);
      }
    },
    // 获取上一条消息和当前消息发送时间是否相差60秒
    getTime(sendTime, previousSendTime) {
      return (new Date(sendTime).getTime() - new Date(previousSendTime.sendTime).getTime()) / 1000 > 60;
    },
    // 初始化聊天记录滚动条到底部
    handlerMessageScrollDown() {
      this.$nextTick(() => {
        if (this.$refs["messageRecordScrollbarRef"]) {
          this.$refs["messageRecordScrollbarRef"].wrap.scrollTop = this.$refs["messageRecordScrollbarRef"].wrap.scrollHeight;
        }
      });
    },
    // 聊天记录滚动事件
    async handleScroll(event) {
      console.log("聊天记录滚动事件:");
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
.chat-box-content {
  display: flex;
  /* 设置主轴的方向 */
  flex-direction: column;
  justify-content: space-between;

  width: $contentWidth;
  height: 100%;

  /* 聊天框顶部用户头像相关信息 */
  .chat-box-head {
    height: 70px;
    justify-content: space-between;
    /* 垂直居中对齐 */
    align-items: center;
    padding: 1rem;

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
      /* 垂直居中对齐 */
      align-items: center;

      i {
        display: flex;
        /* 垂直居中对齐 */
        align-items: center;
        /* 水平居中对齐 */
        justify-content: center;
        cursor: pointer;
        width: 50px;
        font-size: 20px;
      }
    }

  }

  /* 上滑加载 */
  .chat-box-body {
    flex: 1;
    padding-top: 20px;

    .chat-box-body-loading {
      margin-top: 10px;
      display: flex;
      /* 水平居中对齐 */
      justify-content: center;
    }

    .chat-box-body-record {
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
          /* 水平居中对齐 */
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
            /* 解析换行符 */
            white-space: pre-wrap;

            ::v-deep .in-img {
              width: 2rem !important;
              height: 2rem !important;
              line-height: 2rem;
              vertical-align: text-bottom;
            }
          }

          /* 消息已读未读 */
          .message-read {
            display: flex;
            /*
              end value has mixed support: 处理一个CSS属性时，end这个值的支持程度在不同的浏览器中是混合的，有的浏览器支持，有的浏览器不支持。
              consider using flex-end instead: 建议使用flex-end替代原有的值。
            */
            // align-items: end;
            align-items: flex-end;
            margin-right: 3px;

            .unread {
              font-size: 14px !important;
            }

            .read {
              font-size: 13px !important;
              color: green;
            }

            &:hover {
              cursor: pointer
            }
          }
        }
      }

      /* 发送的消息样式，布局在最右侧 */
      .send {
        .message-content {
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
  .chat-box-footer {
    background-color: #F0F8FF1D;
    /* 输入框 */
    border-top: 1px solid #70809054;

    .footer-box-heard {
      position: relative;
      display: flex;

      .action {
        display: flex;
        /* 垂直居中对齐 */
        align-items: center;
        /* 水平居中对齐 */
        justify-content: center;
        margin: 10px 0 10px 10px;
        padding: 5px;
        width: 40px;
        height: 40px;
        /* 鼠标样式 */
        &:hover {
          cursor: pointer;
          background-color: rgba(217, 217, 217, 0.25);
          border-radius: 30%;
        }
      }


      i {
        font-size: 25px !important;
      }
    }

    .footer-box-content {
      /* 输入框 */
      .input-box {
        overflow: hidden;
        overflow-y: auto;
        outline: none;
        padding: 5px 5px 10px 20px;
        line-height: 2rem;
        height: 12rem;
        font-size: 1.3rem !important;
        /* 设置文字的强制自动换行，但只对英文起作用，以字母作为换行依据。 */
        word-break: break-all;
        /* 设置文字的强制自动换行，但只对英文起作用，以单词作为换行依据。 */
        word-wrap: break-word;

        ::v-deep .in-img {
          width: 2rem !important;
          height: 2rem !important;
          line-height: 2rem;
          vertical-align: text-bottom;
        }
      }
    }
  }
}
</style>

<style lang="css">
/*
  已读未读提示
.el-tooltip__popper无效问题，修改elementui自带样式的话，不能在<style scoped><style>中修改，因为不会生效。
*/
.el-tooltip__popper {
  width: 50px;
  text-align: center;
}
</style>