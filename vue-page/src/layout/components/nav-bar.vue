<template>
  <div class='navbar-container'>
    <!-- 导航栏 -->
    <a href="#" class="navbar-logo">
      <img src="../../assets/icons/logo.png" width="50" height="50" style="margin-right: 10px;border-radius: 50px"
           alt="home">
      <h1>Chat</h1>
    </a>
    <div class="navbar-nav">
      <el-dropdown trigger="click" class="navbar-nav-dropdown" :modal-append-to-body="false">
        <div class="navbar-nav-message">
          <span>{{ userInfo.name }}</span>
          <div class="user-avatar">
            <img v-if="userInfo.avatar" :src="userInfo.avatar" alt=""/>
            <template v-else>
              <img v-if="userInfo.sex === 0" src="@/assets/images/avatar/woman.png" alt=""/>
              <img v-if="userInfo.sex === 1" src="@/assets/images/avatar/man.png" alt=""/>
              <img v-if="userInfo.sex === 2" src="@/assets/images/avatar/default.jpg" alt=""/>
            </template>
          </div>
        </div>
        <el-dropdown-menu class="navbar-nav-dropdown-menu" slot="dropdown">
          <el-dropdown-item>
            <div @click="handlerUploadAvatar">
              上传头像
            </div>
          </el-dropdown-item>
          <el-dropdown-item>
            <div @click="handlerSwitchUser">
              切换用户
            </div>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <!-- 上传头像 -->
    <el-dialog append-to-body title="上传头像" class="upload-avatar-dialog" :visible="avatarVisible" width="400px"
               :before-close="handleAvatarClose">
      <!-- 文件上传 -->
      <el-upload
          drag
          action=""
          v-model="imgUrl"
          :class="imgUrl ? '':'avatar-uploader'"
          :auto-upload="false"
          :show-file-list="false"
          :on-change="handlerChangeUpload"
      >
        <img v-if="imgUrl" :src="imgUrl" class="upload-avatar" alt="">
        <div v-else>
          <i class="el-icon-upload avatar-uploader-icon"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传jpg/png/jpeg文件，且不超过5M</div>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleAvatarClose">取 消</el-button>
        <el-button size="small" type="primary" @click="handlerAvatarConfirm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 裁剪图片 -->
    <el-dialog append-to-body class="image-cropper-dialog" title="裁剪图片" :visible="cropperVisible" width="700px"
               :before-close="handleCropperClose">
      <!--
            图片裁剪:https://github.com/xyxiao001/vue-cropper
            img: '',              // 裁剪图片的地址                         空                     url 地址, base64, blob
            outputSize: '',       // 裁剪生成图片的质量                      1                           0.1 ~ 1
            outputType: '',       // 裁剪生成图片的格式                      jpg (jpg 需要传入jpeg)       jpeg, png, webp
            info: '',             // 裁剪框的大小信息                        true                         true, false
            canScale: '',         // 图片是否允许滚轮缩放                     true                         true, false
            autoCrop: '',         // 是否默认生成截图框                      false                       true, false
            autoCropWidth: '',    // 默认生成截图框宽度                      容器的 80%                       0 ~ max
            autoCropHeight: '',   // 默认生成截图框高度                      容器的 80%                       0 ~ max
            fixed: '',            // 是否开启截图框宽高固定比例                false                         true, false
            fixedNumber: '',      // 截图框的宽高比例, 开启fixed生效           [1, 1]                     [ 宽度 ,  高度 ]
            full: '',             // 是否输出原图比例的截图                   false                        true, false
            fixedBox: '',         // 固定截图框大小                          不允许改变                         false
            canMove: '',          // 上传图片是否可以移动                     true                        true, false
            canMoveBox: '',       // 截图框能否拖动                          true                        true, false
            original: '',         // 上传图片按照原始比例渲染                  false                      true, false
            centerBox: '',        // 截图框是否被限制在图片里面                 false                      true, false
            high: '',             // 是否按照设备的dpr 输出等比例图片           true                        true, false
            infoTrue: '',         // 是否展示真实输出图片宽高/展示看到的截图框宽高  false                     true, false
            maxImgSize: '',       // 限制图片最大宽度和高度                    2000                          0 ~ max
            enlarge: '',          // 图片根据截图框输出比例倍数                  1                      0 ~ max(建议不要太大不然会卡死的呢)
            mode: '',             // 图片默认渲染方式                         contain              contain , cover, 100px, 100% auto
            limitMinSize: '',     // 裁剪框限制最小区域                        10                      Number, Array, String
            fillColor: '',        // 导出时背景颜色填充                        空                         #ffffff, white
            // 可用回调方法
            // @realTime 实时预览事件
            // @imgMoving 图片移动回调函数
            // @cropMoving 截图框移动回调函数
            // @imgLoad 图片加载的回调, 返回结果 success, error
        -->
      <div :class="circular  ? 'image-cropper-circular' :''">
        <vue-cropper
            class="image-cropper"
            ref="imageCropper"
            :img="file"
            :output-size="1"
            :output-type="'png'"
            :info="true"
            :auto-crop-width="400"
            :auto-crop-height="400"
            :auto-crop="true"
            :fixed="false"
            :fixed-number="[1, 1]"
            :fixed-box="false"
            :centerBox="true"
        />
      </div>
      <!-- 裁剪操作按钮-->
      <div class="image-cropper-action">
        <el-button class="action" round size="small" plain @click="handleRotateLeft">左旋转</el-button>
        <el-button class="action" round size="small" plain @click="handleRotateRight">右旋转</el-button>
        <el-button class="action" round size="small" plain @click="handleChangeScale(1)">放大</el-button>
        <el-button class="action" round size="small" plain @click="handleChangeScale(-1)">缩小</el-button>
        <el-button class="action" round size="small" plain @click="handleDownload('blob')">下载</el-button>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCropperClose">取 消</el-button>
        <el-button type="primary" @click="handleCropperConfirm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 切换登陆用户 -->
    <el-dialog append-to-body class="switch-user-dialog" :title="'当前登陆人：'+ userInfo.name"
               :visible="userVisible"
               width="300px"
               :before-close="handleUserClose">
      <el-select v-model="userId" ref="select" clearable placeholder="请选择用户">
        <template v-for="(ele,index) in offlineUserOption">
          <el-option :key="index" v-bind:id="ele.id" :label="ele.name" :value="ele.id">
            <div class="user-item">
              <img class="user-item-avatar" v-if="ele.avatar" :src="ele.avatar" alt=""/>
              <template v-else>
                <img v-if="ele.sex === 0" src="@/assets/images/avatar/woman.png" class="user-item-avatar" alt=""/>
                <img v-if="ele.sex === 1" src="@/assets/images/avatar/man.png" class="user-item-avatar" alt=""/>
                <img v-if="ele.sex === 2" src="@/assets/images/avatar/default.jpg" class="user-item-avatar" alt=""/>
              </template>
              <div>{{ ele.name }}</div>
            </div>
          </el-option>
        </template>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleUserClose">取 消</el-button>
        <el-button size="small" type="primary" @click="handlerUserConfirm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {VueCropper} from "vue-cropper";
import {getInfo, updateAvatar} from "@/api/sys/user";
import {upload} from "@/api/sys/attachment";
import {mapGetters} from "vuex";

export default {
  name: "nav-bar-container",
  components: {
    VueCropper
  },
  props: {
    offlineUserOption: {
      type: Array
    }
  },
  data() {
    return {
      // 切换离线用户id
      userId: undefined,
      // 切换用户弹窗状态
      userVisible: false,
      // 上传头像弹窗状态
      avatarVisible: false,
      // 裁剪头像弹窗状态
      cropperVisible: false,
      // 是否裁剪圆形框，默认圆形
      circular: true,
      // 上传的文件
      file: "",
      // 存放文件名
      fileName: "",
      // 图片上传id
      imgId: "",
      // 图片路径
      imgUrl: ""
    };
  },
  computed: {
    ...mapGetters(["userInfo"])
  },
  watch: {},
  created() {
  },
  mounted() {
  },
  methods: {
    /* 修改头像相关事件 */
    // 上传头像事件
    handlerChangeUpload(file) {
      const isLt5M = file.size / 1024 / 1024 < 5;
      if (!isLt5M) {
        this.$message.error("上传头像图片大小不能超过5MB");
        return false;
      }
      const img = file.name.substring(file.name.lastIndexOf(".") + 1);
      const suffix = img === "jpg" || img === "png" || img === "jpeg" || img === "JPG" || img === "PNG" || img === "JPEG";
      if (!suffix) {
        this.$message.error("只能上传jpg/png/jpeg文件");
        return false;
      }
      // URL.createObjectURL的参数只能是blob或者file类型
      // 第一种方法用FileReader，URL.createObjectURL接收blob类型
      const reader = new FileReader();
      reader.onload = () => {
        // 把Array Buffer转化为blob 如果是base64不需要
        if (typeof reader.result === "object") {
          this.file = window.URL.createObjectURL(new Blob([reader.result]));
        } else {
          this.file = reader.result;
        }
      };
      // 转化为base64
      reader.readAsArrayBuffer(file.raw);
      // 第二种方法，URL.createObjectURL接收file类型
      // this.$nextTick(() => {
      //   this.file = URL.createObjectURL(file.raw)
      //   this.cropperVisible = true
      // })
      this.fileName = file.name;
      this.cropperVisible = true;
    },
    // 确认修改头像
    handlerAvatarConfirm() {
      if (this.imgId) {
        updateAvatar(this.imgId).then(res => {
          if (res.data.code === 200 && res.data.data) {
            // 刷新用户信息
            getInfo().then(res => {
              if (res.data.code === 200 && res.data.data) {
                this.$store.dispatch("user/setUserInfo", res.data.data);
              } else {
                this.$message.error(res.data.msg);
              }
            });
            this.handleAvatarClose();
          } else {
            this.$message.error(res.data.msg);
          }
        });
      }
    },
    // 打开上传头像弹窗
    handlerUploadAvatar() {
      this.avatarVisible = true;
      if (this.userInfo.avatar) {
        this.imgUrl = this.userInfo.avatar;
      } else {
        if (this.userInfo.sex === 0) {
          this.imgUrl = "http://localhost/attachment/download/088d93736b6290b1b2149e8056140b2f.png";
        } else if (this.userInfo.sex === 1) {
          this.imgUrl = "http://localhost/attachment/download/78190f0fbe558af32b52da98768d4ef2.png";
        } else {
          this.imgUrl = "";
        }
      }
    },
    // 关闭上传头像弹窗
    handleAvatarClose() {
      this.avatarVisible = false;
    },
    // 确认裁剪头像
    handleCropperConfirm() {
      this.$refs.imageCropper.getCropBlob(async (blob) => {
        const form = new FormData();
        let file;
        // 处理圆形
        if (this.circular) {
          const reader = new FileReader();
          reader.readAsDataURL(blob);
          reader.onload = e => {
            const src = e.target.result;
            const image = new Image();
            image.src = src;
            image.onload = async () => {
              const canvas = document.createElement("canvas");
              const width = image.width;
              const height = image.height;
              canvas.width = width;
              canvas.height = height;
              // 计算圆形图片的圆心及图片半径
              const circle = {
                x: width / 2,
                y: height / 2,
                r: width / 2
              };
              const context = canvas.getContext("2d");
              context.clearRect(0, 0, width, height);
              // 在canvas开始绘制前填充白色透明背景并设置透明度，用以清除图片裁剪后透明区域变成黑色的问题
              context.fillStyle = "rgba(255, 255, 255, 0)";
              context.fillRect(0, 0, width, height);

              // 开始路径画圆，剪切处理
              context.save(); // 保存当前canvas的状态
              context.beginPath();
              context.arc(circle.x, circle.y, circle.r, 0, Math.PI * 2, false); // 创建弧/曲线(用于创建圆形或部分圆)
              context.clip(); // 从原始画布剪切任意形状和尺寸的区域
              context.drawImage(image, 0, 0);
              context.restore(); // 返回之前保存过的路径状态和属性，恢复状态

              console.log("3333");
              // 将canvas图片转换成 blob数据
              canvas.toBlob(blobData => {
                file = new File([blobData], this.fileName, {type: blobData.type, lastModified: Date.now()});
              });
            };
            image.onerror = err => {
              console.log("image err", err);
            };
          };
          reader.onerror = err => {
            console.log("reader err", err);
          };
        } else {
          // new File()的第一个参数是一个字符串数组，数组中的每一个元素对应着文件中一行的内容
          // 第二个参数就是文件名字符串
          // 第三个参数可以设定一些文件的属性，比如文件的MIME，最后更新时间等
          file = new File([blob], this.fileName, {type: blob.type, lastModified: Date.now()});
        }
        // 等待文件转换完成
        setTimeout(async () => {
          console.log("444");
          file.uid = Date.now();
          form.append("files", file);
          // 如果想在这里打印查看form的值，会发现它是空对象
          // 解决办法，需要用form.get('键')的方法获取值
          // console.log(form.get('file'));
          // 这里调用接口，获取后端返给的图片地址
          const res = await upload(form);
          if (res.data.code === 200 && res.data.data) {
            // 返回的数组
            const attachment = res.data.data[0];
            this.imgUrl = attachment.url;
            this.imgId = attachment.id;
            this.handleCropperClose();
          } else {
            this.$message.error(res.data.msg);
          }
        }, 200);
      });
    },
    // 关闭裁剪头像弹窗
    handleCropperClose() {
      this.cropperVisible = false;
    },
    // 裁剪头像放大/缩小
    handleChangeScale(num) {
      num = num || 1;
      this.$refs.imageCropper.changeScale(num);
    },
    // 裁剪头像左旋转
    handleRotateLeft() {
      this.$refs.imageCropper.rotateLeft();
    },
    // 裁剪头像右旋转
    handleRotateRight() {
      this.$refs.imageCropper.rotateRight();
    },
    // 裁剪头像下载
    handleDownload(type) {
      let aLink = document.createElement("a");
      aLink.download = this.fileName;
      if (type === "blob") {
        this.$refs.imageCropper.getCropBlob((data) => {
          window.URL.createObjectURL(data);
          aLink.href = window.URL.createObjectURL(data);
          aLink.click();
        });
      } else {
        this.$refs.imageCropper.getCropData((data) => {
          aLink.href = data;
          aLink.click();
        });
      }
    },

    /* 切换用户相关事件 */
    // 打开切换用户弹窗
    handlerSwitchUser() {
      this.$emit("getOfflineUserList");
      this.userVisible = true;
    },
    // 关闭切换用户弹窗
    handleUserClose() {
      this.userVisible = false;
    },
    // 确认切换登陆用户
    handlerUserConfirm() {
      this.offlineUserOption.map(ele => {
        if (this.userId === ele.id) {
          this.userId = undefined;
          this.$store.dispatch("user/setUserInfo", ele);
          this.$store.dispatch("message/delRecentlyMessageActivate");
          this.$store.dispatch("message/delRecentlyMessageMap");
          this.$store.dispatch("message/delMessageMap");
        }
        // 刷新当前页面
        window.location.reload();
      });
      this.userVisible = false;
    }

  }
};
</script>

<style scoped lang='scss'>
/* 顶部导航栏 */
.navbar-container {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 10px;
  border-bottom: 1px solid #70809054 !important;
  /* box-shadow: 水平阴影 垂直阴影 模糊距离 阴影尺寸 阴影颜色 内外阴影; */
  box-shadow: 0 3px 3px rgba(0, 0, 0, .075) !important;

  .navbar-logo {
    display: flex;
    /* 垂直居中对齐 */
    align-items: center;
    /* 背景色设置为透明 */
    background-color: transparent;
    margin-left: 10px;
  }

  .navbar-nav {
    .navbar-nav-dropdown {
      display: flex;

      .navbar-nav-message {
        font-size: 18px;
        display: flex;
        /* 垂直居中对齐 */
        align-items: center;

        span {
          margin-right: 10px;
          font-size: 14px;
        }

        .user-avatar {
          img {
            margin-right: 20px;
            display: block;
            width: 50px;
            height: 50px;
            border-radius: 50%;
          }
        }
      }

      //::v-deep .navbar-nav-dropdown-menu {
      //  width: 300px !important;
      //}


    }
  }
}

//::v-deep .el-dropdown-menu {
//  width: 100px;
//}

/* 上传头像弹窗样式 */
.upload-avatar-dialog {
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  /* 上传后的头像预览样式 */
  .upload-avatar {
    margin-left: 90px;
    width: 180px;
    height: 180px;
    display: block;
  }

  .el-upload-dragger {
    border: none;
  }
}

/* 裁剪图片 */
.image-cropper-dialog {
  .image-cropper {
    height: 500px;
    width: auto;
  }

  /* 圆形裁剪框 */
  .image-cropper-circular .cropper-view-box {
    /*   将裁剪框由方形调整为圆形 */
    border-radius: 50%;
  }

  /* 裁剪操作按钮 */
  .image-cropper-action {
    display: flex;
    /* 水平居中对齐 */
    justify-content: center;
    margin-top: 30px;

    .action {
      margin-right: 10px;
      box-shadow: 0 1px 1px rgba(0, 0, 0, 0.3);
    }
  }

  .cropper-face {
    /*  清除裁剪框填充背景色  */
    background-color: transparent !important;
  }
}

/* 切换用户 */
.switch-user-dialog {
  .el-select {
    margin-left: 40px;
  }
}

/* 切换用户下拉选项 */
.user-item {
  display: flex;
  align-items: center;

  .user-item-avatar {
    width: 20px !important;
    height: 20px !important;
    margin-right: 15px;
    border-radius: 50%;
  }
}
</style>