package com.hsj.protocol;

import com.hsj.common.Invocation;
import com.hsj.register.LocalRegister;
import com.hsj.utils.HessianUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        //通过反序列化获得客户端方法调用的invocation
        Invocation invocation = (Invocation) HessianUtils.deserialize(bytes);
        String interfaceName = invocation.getInterfaceName();

        //从本地注册表中获取接口的实现类
        Class classImpl = LocalRegister.get(interfaceName);

        //从实现类中获取需要执行的方法
        Method method = classImpl.getMethod(invocation.getMethodName(), invocation.getParameterTypes());

        //通过反射执行方法 在指定的对象(第一个参数是实现类的对象)上调用这个方法，并传入参数。
        String Result = (String) method.invoke(classImpl.newInstance(),invocation.getParameters());
        System.out.println(Result);

        // 将结果返回给客户端
        ByteBuf responseBuf = ctx.alloc().buffer();
        byte[] responseBytes = HessianUtils.serialize(Result);  // 序列化响应对象
        responseBuf.writeBytes(responseBytes);  // 将字节写入到响应缓冲区

        // 发送响应数据给客户端使用ChaannelHandlerContext的writeFlush方法
        ctx.writeAndFlush(responseBuf).addListener(ChannelFutureListener.CLOSE);  // 发送并关闭连接
    }
}
