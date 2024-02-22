<template>
  <!-- 用户信息 -->
  <div class="user-info-container">
    <div class="user-info">
      <div class="header">
        <img :src="contactsInfo.avatar" class="avatar" alt="">
        <div class="header-info">
          <div class="info">
            <div class="name">
              <span>{{ contactsInfo.name }}</span>
            </div>
            <div class="num">
              <span>微信号：123456</span>
            </div>
          </div>
          <div class="action">
            <i class="el-icon-more"></i>
          </div>
        </div>
      </div>
      <div class="content">
        <!-- 分割线 -->
        <el-divider></el-divider>
        <div>
          <div class="remark">
            备注
            <div>
              <el-input :value="contactsInfo.remark"></el-input>
            </div>
          </div>

        </div>
        <el-divider></el-divider>
        <div>
          <div class="left">
            朋友圈
          </div>
        </div>
        <el-divider></el-divider>
        <div>
          <div class="left">
            共同群聊 {{ contactsInfo.count || 0 }}个
          </div>
          <div class="left">
            来源
          </div>
          <el-divider></el-divider>
        </div>
      </div>
      <div class="footer">
        <div class="send-message action" @click="handlerClick(1)">
          <i class="icon-xiaoxi"></i>
          发消息
        </div>
        <div class="voice-call action" @click="handlerClick(2)">
          <i class="icon-tonghuajilu-tonghuajieshu"></i>
          语音通话
        </div>
        <div class="video-ccall action" @click="handlerClick(3)">
          <i class="icon-video"></i>
          视频通话
        </div>
      </div>

    </div>
  </div>
</template>

<script>

import {getInfoById} from "@/api/chat/contacts";
import {mapGetters} from "vuex";

export default {
  name: "user-info-container",
  components: {},
  props: {},
  data() {
    return {
      contactsInfo: {
        id: undefined,
        name: "",
        avatar: "",
        remark: "",
        count: 0
      }
    };
  },
  computed: {
    ...mapGetters(["contactsActivate"])
  },
  watch: {},
  created() {
  },
  updated() {
  },
  mounted() {
    this.getContactsInfo();
  },
  methods: {
    getContactsInfo() {
      getInfoById(this.contactsActivate.id).then(res => {
        if (res.data.code === 200 && res.data.data) {
          this.contactsInfo = res.data.data;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    handlerClick(type) {
      // 发消息
      if (type === 1) {
        // 把聊天人添加到最近消息第一位，页面跳转到消息
      }
    }
  }
};
</script>

<style scoped lang='scss'>
.user-info-container {
  display: flex;
  flex: 1;
  /* 设置主轴的方向 */
  flex-direction: column;
  justify-content: space-between;

  .user-info {
    padding: 20px;
    margin: 30px 0 0 100px;
    width: 500px;

    .header {
      display: flex;
      align-items: center;

      .avatar {
        width: 40px !important;
        height: 40px !important;
        border-radius: 50%;
      }

      .header-info {
        display: flex;
        flex: 1;
        align-items: center;
        justify-content: space-between;
        padding: 20px 0 0 20px;

        .info {
          .name {
            padding: 10px 0;
          }

          .num {

          }
        }
      }

      .action {

      }
    }

    .content {

    }

    .footer {
      display: flex;
      align-items: center;
      text-align: center;

      .action {
        padding: 20px 50px 0 50px;

        &:hover {
          cursor: pointer
        }

        i {
          display: block;
        }

        .send-message {
          //align-items: center;
          //justify-content: center;
          //flex-direction: column;
        }

        .voice-call {

        }
      }

    }
  }
}
</style>