<template>
  <!-- 表情组件 -->
  <div class="emoji-dialog-container">
    <div class="emoji-box">
      <img
          v-for="(ele, index) in emojiData"
          :key="index"
          :src="ele.src"
          :title="ele.name"
          @click.stop="handlerEmojiClick(ele)"
          alt=""/>
    </div>
  </div>
</template>

<script>

import {emojiData} from "@/utils/emoji";

export default {
  name: "emoji-dialog-container",
  components: {},
  props: {},
  data() {
    return {
      emojiData: emojiData
    };
  },
  computed: {},
  watch: {},
  created() {
  },
  updated() {
  },
  mounted() {
  },
  methods: {
    handlerEmojiClick(ele) {
      this.$emit("handlerEmojiClick", ele);
    }
  }
};
</script>

<style scoped lang='scss'>
.emoji-dialog-container {

  /* 表情列表盒子 */
  .emoji-box {
    justify-content: flex-start;
    position: absolute;
    z-index: 999;
    top: 0;
    left: 0;
    /* 向上偏移 */
    transform: translateY(-102%);
    padding: 10px;
    width: 300px;
    height: 250px;
    overflow: hidden;
    overflow-y: scroll;
    box-shadow: 0 0 5px #ececec;
    /* 是否能选取元素的文本 */
    user-select: none;
    background-color: white;

    img {
      /*  1、ease 默认值，慢速开始，快速变快，慢速结束
          2、linear 匀速
          3、ease-in 慢速开始，加速结束
          4、ease-out 快速开始，减速结束
          5、ease-in-out 慢速开始和结束，中间先加后减
      */
      transition: transform 0.3s ease;
      width: 2rem;
      height: 2rem;
      margin: 6px;

      &:hover {
        transform: scale(1.2);
      }
    }
  }
}
</style>