package com.cmmplb.websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author penglibo
 * @date 2021-06-19 14:19:02
 * @since jdk 1.8
 */

@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 在这个类里面我们首先建立了一个channelGroup，
    // 每当有客户端连接的时候，就添加到channelGroup里面，
    // 我们可以发送消息给固定的人，也可以群发消息。
    public static ChannelGroup channelGroup;

    static {
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    // 客户端与服务器建立连接的时候触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启！");
        System.out.println(channelGroup);
        // 添加到channelGroup通道组
        channelGroup.add(ctx.channel());
    }


    /**
     * 当web客户端连接后，触发方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        /**
         * 这个ID是唯一的
         */
        log.info(">>>>>>>>>>>> channelId:{} 连接",ctx.channel().id().asLongText());

        /**
         *  这个ID不是唯一的
         */
        log.info(">>>>>>>>>>>> channelId:{} 连接",ctx.channel().id().asShortText());
        sendAllMessage();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info(">>>>>>>>>>>>> channelId:{} 关闭了",ctx.channel().id().asLongText());
    }


    // 客户端与服务器关闭连接的时候触发
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭！");
        channelGroup.remove(ctx.channel());
    }

    // 服务器接收客户端的数据信息
    @Override
    // 5.0 核心改变
    // -简化 handler 类型继承关系
    // -channelRead0() → messageReceived()
    // -更灵活的线程模型
    // -更好的Channel.deregister(...)
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.println("服务器收到的数据:" + textWebSocketFrame.text());
        sendAllMessage();
        /**
         * 回复消息
         */
        channelHandlerContext.writeAndFlush(new TextWebSocketFrame("服务器收到了,并返回：" + textWebSocketFrame.text()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(">>>>>>>>发送异常：{}",cause.getMessage());
        ctx.close();
    }

    // 给固定的人发消息
    private void sendMessage(ChannelHandlerContext ctx) {
        String message = "你好," + ctx.channel().localAddress() + " 给固定的人发消息";
        ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
    }

    // 发送群消息，此时其他客户端也能收到群消息
    private void sendAllMessage() {
        String message = "我是服务器，这里发送的是群消息";
        channelGroup.writeAndFlush(new TextWebSocketFrame(message));
    }

    // @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.println("服务器收到的数据:" + textWebSocketFrame.text());
    }
}
