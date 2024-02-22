<template>
  <!-- 用户信息 -->
  <div class="user-info-container">
    <div class="user-info">
      <div class="header">
        <img src="ele.avatar" class="avatar" alt="">
        <div class="action">

        </div>
      </div>
      <div class="content">
        <!-- 分割线 -->
        <el-divider></el-divider>
        <div>
          <div class="left">
            备注
          </div>
          <div class="right">
            <el-input></el-input>
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
            共同群聊
          </div>
          <div class="right">
            0个
          </div>
          <div class="left">
            来源
          </div>
          <div class="right">

          </div>
        </div>
      </div>
      <div class="footer">
        <div>
          发消息
        </div>
        <div>
          语音通话
        </div>
        <div>
          视频通话
        </div>
      </div>

    </div>
  </div>
</template>

<script>

export default {
  name: "user-info-container",
  components: {},
  props: {
    userId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {};
  },
  computed: {

  },
  watch: {},
  created() {
  },
  updated() {
  },
  mounted() {
  },
  methods: {}
};
</script>

<style>

</style>