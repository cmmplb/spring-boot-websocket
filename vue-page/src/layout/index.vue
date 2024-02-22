<template>
  <div class="app-wrapper">
    <!-- 导航栏 -->
    <!--        <NavBar :offlineUserOption="offlineUserOption" @getOfflineUserList="getOfflineUserList"/>-->
    <!--    :w="dragStyles.w"  默认宽度-->
    <!--		:h="dragStyles.h" 默认高度-->
    <!--		:min-width="250" 最小宽度-->
    <!--		:min-height="290"最小高度-->
    <!--		:max-width="1900" 最大宽度-->
    <!--		:max-height="1000" 最大高度-->
    <!--		:parent="false" 限制不能拖出父元素-->
    <!--    parent=".p-event" 限制不能拖出class为p-event的元素-->
    <!--    :group= "name" 相同的组之间可以相互拖拽-->
    <!--    :delay= "0" 鼠标按下后多久可以拖拽-->
    <!--		:active="true" 确定组件是否应处于活动状态。-->
    <!--		:grid 水平和垂直移动 每次分别能够走多少像素-->
    <!--		:x="dragStyles.x" 默认水平坐标 注意相对元素是谁-->
    <!--		:y="dragStyles.y" 默认垂直坐标 注意相对元素是谁-->
    <!--		:z="1001" 层级-->
    <!--		class-name="tulie"  自定义组件class-->
    <!--		class-name-handle="my-handle-class"-->
    <!--		drag-handle=".title"定义应该用于拖动组件的选择器。-->
    <!--    :draggable="false"定义组件应该是否可拖动。 -->
    <!--    :resizable="false"定义组件是否可以调整大小。 -->
    <!--		drag-cancel=".table-container" 定义应该用于阻止拖动初始化的选择器-->
    <!--		axis="x" 定义元素可拖动的轴。可用值为x, y 或者 both-->
    <!--		:grid="[1,1]" 定义捕捉元素的网格。-->
    <!--		:handles="['tl','tr','bl','br']" 定义缩放的句柄（共八个方向）:tl:上左，tm:上中，tr:上右，mr:中左，ml:中右，bl:下左，bm:下中，br:下右-->
    <vue-draggable-resizable :w="width"
                             :h="height"
                             :x="400"
                             :y="50"
                             :handles="['tl','tr','bl','br']"
                             v-on:dragging="onDrag"
                             v-on:resizing="onResize"
                             :draggable="true"
                             :resizable="false"
                             :preventActiveBehavior="true"
                             :style="{width: width+'px',height:height+'px'}">
      <div class="app-wrapper-content">
        <!-- 左侧菜单 -->
        <SideBar :offlineUserOption="offlineUserOption" @getOfflineUserList="getOfflineUserList"/>

        <!-- 消息信息 -->
        <MessageInfo @handlerMessageUser="handlerMessageUser"/>

      </div>
    </vue-draggable-resizable>
    <!---->
  </div>
</template>

<script>

import {mapGetters} from "vuex";
import NavBar from "@/layout/components/nav-bar.vue";
import SideBar from "@/layout/components/side-bar.vue";
import MessageInfo from "@/layout/components/main-content/index.vue";
import {getOfflineUserList} from "@/api/sys/user";
import websocket from "@/utils/websocket";
import VueDraggableResizable from "vue-draggable-resizable";
import "vue-draggable-resizable/dist/VueDraggableResizable.css";

export default {
  name: "chat-vue",
  components: {
    VueDraggableResizable,
    NavBar,
    SideBar,
    MessageInfo
  },
  props: {},
  data() {
    return {
      width: 1200,
      height: 980,
      offlineUserOption: [],
      // 激活的导航菜单:message-消息;addressBook-通讯录;find-发现;
      navActivate: "find"
    };
  },
  computed: {
    ...mapGetters(["userInfo", "recentlyMessageMap", "recentlyMessageActivate"])
  },
  watch: {},
  created() {
  },
  mounted() {
    this.getUserInfo();
  },
  beforeDestroy() {
    window.addEventListener("beforeunload", this.handleWindowClose); // 添加监听器
  },
  methods: {
    // 获取用户信息
    async getUserInfo() {
      console.log("this.this.userInfo:", this.userInfo);
      console.log("this.offlineUserOption:", this.offlineUserOption);
      if (!this.userInfo.id) {
        await this.getOfflineUserList();
        // 从离线用户里面随机取一个用户
        if (this.offlineUserOption.length > 0) {
          const randomIndex = Math.floor(Math.random() * this.offlineUserOption.length);
          const user = this.offlineUserOption[randomIndex];
          await this.$store.dispatch("user/setUserInfo", user);
        }
        // 排除自己
        this.offlineUserOption = this.offlineUserOption.filter(ele => ele.id !== this.userInfo.id);
      }
      // 获取到用户信息后建立连接
      websocket.createWebSocket(this.userInfo.id);
    },
    // 获取离线用户列表
    getOfflineUserList() {
      return getOfflineUserList().then(res => {
        if (res.data.code === 200 && res.data.data) {
          this.offlineUserOption = [];
          res.data.data.map(ele => {
            if (this.userInfo && this.userInfo.id !== ele.id) {
              this.offlineUserOption.push(ele);
            }
          });
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    // 点击最近消息用户
    handlerMessageUser() {
      this.$nextTick(() => {
        if (this.$refs.chatBoxRef) {
          this.$refs.chatBoxRef.handlerMessageUser();
        }
      });
    },
    // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    handleWindowClose(event) {
      event.preventDefault(); // 取消默认行为
      websocket.close();
    },
    onResize: function (x, y, width, height) {
      this.x = x;
      this.y = y;
      console.log("width:", width);
      console.log("height:", height);
      this.width = width;
      this.height = height;
      console.log("1");
    },
    onDrag: function (x, y) {
      this.x = x;
      this.y = y;
      console.log("2");
    }
  }
};

</script>

<style scoped lang='scss'>
/* 页面flex布局 */
.app-wrapper {
  /* vh指的是视口高度，vw指的是视口宽度 */
  height: 100vh;
  width: 100vw;
  background-image: url('@/assets/images/background/sea.jpeg');
  background-size: cover;
  background-repeat: no-repeat;
  //filter: blur(5px);

  .app-wrapper-content {
    display: flex;
    width: 100%;
    height: 100%;
    border: 1px solid #70809054 !important;
    background-color: #ffffff;
    /* 边框阴影 */
    box-shadow: 0 0 30px 10px rgba(0, 0, 0, .3);
  }
}

// 删除边框
.vdr {
  border: none !important;
}

.app-wrapper::after {
  /* 毛玻璃 */

}
</style>