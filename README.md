# 学习springboot整合websocket、netty

前端：vue+element-ui

版本：

| 依赖         | 版本     |
|------------|--------|
| springboot | 2.5.3  |
| vue        | 2.6.14 |

仓库地址：https://gitee.com/cmmplb/spring-boot-websocket

前端连接websocket和netty的demo学习。

目前只加了首页的样式和上传头像切换功能，学习一下，简单实现通讯录的逻辑，一点一点的加代码。

#### finished

![image_04.png](doc%2Fimages%2Fimage_04.png)

- 单聊
- 是否已读
- 离线消息
- 通讯录


#### todo

- 群聊
- 黑名单

# 项目图片

#### 首页

![image_01.png](doc%2Fimages%2Fimage_01.png)

#### 上传头像

![image_02.png](doc%2Fimages%2Fimage_02.png)

#### 方便学习，没有集成认证，点击右上角直接切换用户。

![image_03.png](doc%2Fimages%2Fimage_03.png)

#### 遇到的问题

1. vue绑定数据修改了但是页面上没刷新，使用vue事件强制更新

````
this.$forceUpdate();
````

2. 设置滚动条位置时不生效，等待dom加载完成

````
nextTick(() => {
    todo:
})
````

3. 上拉滑动加载数据实现，添加滚动事件后，计算距离顶部的距离，为0则到顶触发响应的事件

````
addEventListener("scroll", this.handle);

handle(event){
 console.log(event.target.scrollTop );
}

````

4. group by配合any_value(column)

5. JSONObject.parseObject解析出来的对象属性值是null，要有构造函数

***

2024-01-05
添加双向聊天接收消息功能

***

2024-01-12
添加vuex存储最近消息和聊天记录，发送消息后将聊天用户置顶，同时把每个组件拆分了一下。

***

2024-01-19
添加联系人和表情渲染到输入框的逻辑。
![image_05.png](doc%2Fimages%2Fimage_05.png)

***

2024-01-26
添加websocket心跳检测，断线重连，消息数量，已读未读，表情回显到聊天记录。

2024-02-02
调整了一下布局方式，使用vue-draggable-resizable组件实现窗口拖拽。
![image_06.png](doc%2Fimages%2Fimage_06.png)