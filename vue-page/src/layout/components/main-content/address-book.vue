<template>
  <!-- 联系人 -->
  <div class="contacts-list-container">
    <div class="topping">
      <el-input class="topping-search" v-model="searchForm.keywords" prefix-icon="el-icon-search" clearable
                placeholder="搜索"></el-input>
    </div>
    <template v-if="this.contactsList && this.contactsList.length > 0">
      <!-- el-scrollbar 必须指定高度 -->
      <el-scrollbar class="content-scrollbar">
        <ul class="contacts-list" v-infinite-scroll="handleScrollBottom">
          <li v-for="(ele,index) in contactsList" v-bind:key="index"
              @click="handlerContacts(ele)"
              :style="contactsActivate.id === ele.id ? 'color: #FFA500FF; background-color: #FFA5002A;' : ''"
              class="contacts">
            <img :src="ele.avatar" class="avatar" alt="">
            <div>
              <div class="header">
                <span class="name">{{ ele.name }}</span>
              </div>
            </div>
          </li>
        </ul>
      </el-scrollbar>
    </template>

    <!-- 联系人信息 -->
    <ContactInfo v-if="navActivate === 'addressBook' && contactsActivate.id"/>
  </div>

</template>

<script>

import {getByPaged} from "@/api/chat/contacts";
import {mapGetters} from "vuex";
import ContactInfo from "@/views/chat/contacts/contacts-info.vue";

export default {
  name: "contacts-list-container",
  components: {ContactInfo},
  props: {
    navActivate: {
      type: String
    }
  },
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
      },
      contactsList: []
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
    this.getConcatList();
  },
  methods: {
    getConcatList() {
      getByPaged({...this.pageData}).then(res => {
        if (res.data.code === 200 && res.data.data) {
          this.pageData.total = res.data.data.total;
          this.pageData.totalPage = Math.ceil(this.pageData.total / this.pageData.size);
          this.contactsList = res.data.data.rows;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    handlerContacts(contacts) {
      console.log("contacts:", contacts);
      if (this.contactsActivate.id !== contacts.id) {
        // 设置联系人激活对象
        this.$store.dispatch("contacts/setContactsActivate", contacts);
      }

    },
    // 滚动到底触发事件
    handleScrollBottom() {
      // console.log("111");
    }
  }
};
</script>

<style scoped lang='scss'>

/* 通讯录列表 */
.contacts-list-container {
  display: flex;
  flex-wrap: wrap;
  flex-direction: column;
  //border-right: 1px solid #70809054 !important;

  /* 搜索 */
  .topping {

    padding: 20px;
    /* border-bottom: 1 px solid #70809054 !important; */
    border-right: 1px solid #70809054 !important;
    /* box-shadow: 水平阴影 垂直阴影 模糊距离 阴影尺寸 阴影颜色 内外阴影; */
    /* box-shadow: 1 px 0 1 px rgba(0, 0, 0, .075) !important; */
    ::v-deep .el-input__inner {
      background-color: rgba(112, 128, 144, 0.05);
    }
  }

  /* 联系人列表 */
  .content-scrollbar {
    overflow-y: auto;
    /* 动态计算盒子的高度总高度-nav-bar的高度-搜索栏的高度 */
    height: calc(100% - 70px - 80px);
    /* box-shadow: 水平阴影 垂直阴影 模糊距离 阴影尺寸 阴影颜色 内外阴影; */
    box-shadow: 1px 0 1px rgba(0, 0, 0, .075) !important;

    .contacts-list {
      display: flex;
      /* 设置主轴的方向 */
      flex-direction: column;

      /* 每个联系人样式 */
      .contacts {
        position: relative;
        display: flex;
        align-items: center;
        padding: 20px;
        height: 80px;
        border-bottom: 1px solid #70809054 !important;

        /* 联系人头像 */
        .avatar {
          margin-right: 20px;
          display: block;
          width: 40px !important;
          height: 40px !important;
          border-radius: 50%;
        }

        /* 联系人名称 */
        .name {
          font-size: 16px;
        }

        /* 鼠标移入每个联系人栏样式 */
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