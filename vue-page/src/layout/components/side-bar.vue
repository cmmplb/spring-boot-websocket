<template>
  <!-- 左侧菜单 -->
  <div class="aside-container">
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
        <a class="nav-link" :style="navActivate === 'find' ? 'color: coral' : ''">
          <i class="el-icon-chat-dot-round"></i>
          <span>发现</span>
        </a>
      </li>
    </ul>
  </div>
</template>

<script>

export default {
  name: "side-bar-container",
  components: {},
  props: {
    navActivate: {
      type: String
    }
  },
  data() {
    return {};
  },
  computed: {},
  watch: {},
  created() {
  },
  mounted() {
  },
  methods: {
    // 切换左侧菜单导航
    handlerNav(type) {
      if (type === 1) {
        this.$emit("setNavActivate", "message");
      } else if (type === 2) {
        this.$emit("setNavActivate", "contacts");
      } else {
        this.$emit("setNavActivate", "find");
      }
    }
  }
};
</script>

<style scoped lang='scss'>
/* 左侧菜单条 */
.aside-container {
  display: flex;
  /* 动态计算盒子的高度，70的导航加1的边框 */
  height: calc(100vh - 71px);
  flex-direction: column;
  justify-content: space-between;
  border-right: 1px solid #70809054 !important;
  /* box-shadow: 水平阴影 垂直阴影 模糊距离 阴影尺寸 阴影颜色 内外阴影; */
  box-shadow: 1px 0 1px rgba(0, 0, 0, .075) !important;

  .nav {
    display: flex;
    flex-direction: column;


    .nav-link {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      /* 垂直居中对齐 */
      align-items: center;
      padding: 18px;

      i {
        font-size: 25px;
      }

      span {
        white-space: nowrap;
      }
    }

    .aside-container .nav .nav-link:hover {
      color: coral;
      cursor: pointer
    }

    .nav-item-top {
      margin-top: 20px;
    }

  }
}


</style>