package com.cmmplb.websocket.server;

import com.cmmplb.websocket.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author penglibo
 * @date 2021-06-19 14:01:12
 * @since jdk 1.8
 */

@Slf4j
public class WebSocketServer extends Thread {

    private final int port;

    public void init() {
        log.info("正在启动webSocket服务器……");
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup group = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 设置线程队列得到连接个数
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        // 设置两个线程组
        serverBootstrap.group(group, boosGroup)
                //设置保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)

                //使用NioServerSocketChannel 作为服务器的通道实现
                .channel(NioServerSocketChannel.class)

                .localAddress(port)

                // 给workerGroup 的 EventLoop 对应的管道设置处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        System.out.println("收到新连接:" + socketChannel.localAddress());

                        // 编解码 http 请求,因为基于http协议，使用http的编解码器
                        socketChannel.pipeline().addLast(new HttpServerCodec());
                        // 写文件内容
                        // 是以块方式写，添加 ChunkedWriteHandler 处理器
                        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                        /**
                         * 聚合解码 HttpRequest/HttpContent/LastHttpContent 到 FullHttpRequest
                         * 保证接收的 Http 请求的完整性
                         *  http数据传输过程是分段的，HttpObjectAggregator，就是可以将多段聚合
                         *  当浏览器发送大量数据时，就会发出多次http请求
                         */
                        socketChannel.pipeline().addLast(new HttpObjectAggregator(8192));
                        // 设置链接超时时间
                        socketChannel.pipeline().addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
                        /**
                         * 处理其他的 WebSocketFrame
                         * 对于websocket数据是以 帧 形式传递
                         *  浏览器请求时 ws://localhost:7000/hello 其中 hello会与下面的对应
                         *  WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议，保持长链接
                         */
                        socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", "WebSocket", true, 65536 * 10));
                        // 这个类的代码是模板代码，最核心的就是ch.pipeline().addLast(new MyWebSocketHandler())，
                        // 可以根据自己的需求配置即可
                        socketChannel.pipeline().addLast(new WebSocketHandler());
                    }
                });
        ChannelFuture sync = null; // 服务器异步创建绑定
        try {
            // 启动服务器并绑定一个端口并且同步生成一个 ChannelFuture 对象
            sync = serverBootstrap.bind(port).sync();
            log.info("启动webSocket服务器启动成功，正在监听端口:" + port);
            //对关闭通道进行监听
            sync.channel().closeFuture().sync(); //以异步的方式关闭端口
        } catch (InterruptedException e) {
            log.info("启动出现异常：" + e);
        }
        try {
            group.shutdownGracefully().sync(); // 释放线程池资源
            boosGroup.shutdownGracefully().sync(); // 释放线程池资源
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new WebSocketServer(8002).init();
    }

    public WebSocketServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        init();
    }
}
