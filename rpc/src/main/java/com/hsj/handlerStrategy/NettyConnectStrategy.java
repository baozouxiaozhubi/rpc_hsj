package com.hsj.handlerStrategy;

import com.hsj.protocol.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class NettyConnectStrategy implements ConnectStrategy {
    @Override
    public void connect(String hostname, Integer port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); //用于服务器端接受客户端的连接 1个线程处理所有客户端连接请求
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 默认是CPU核心数*2，用于处理读写，远程服务调用可以视作是I/O密集型
        try
        {
            //Netty启动服务器的核心配置类，用于初始化和启动服务端的各种组件。
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup); //设置处理用户连接请求和实际数据读写，数据处理逻辑的线程池
            bootstrap.channel(NioServerSocketChannel.class); //指定使用NIO模式的服务端通道（基于 Selector 的非阻塞 IO）
            bootstrap.localAddress(hostname,port);
            //用于在每一个新建立的 Socket 连接上初始化 Channel 的逻辑（比如添加编解码器、业务处理器等）。
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();

                    // 添加帧解码器/编码器，处理粘包
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4));
                    pipeline.addLast(new LengthFieldPrepender(4));

                    // 添加自定义的业务处理器
                    pipeline.addLast(new NettyServerHandler());
                }
            });

            ChannelFuture future = bootstrap.bind().sync(); // 绑定端口，启动服务，并同步等待绑定完成
            System.out.println("Netty server started at " + hostname + ":" + port);
            future.channel().closeFuture().sync(); // 阻塞，直到服务器通道关闭

        }
        catch(Exception e){e.printStackTrace();}
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
