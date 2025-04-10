package com.hsj;

import com.hsj.common.Invocation;
import com.hsj.protocol.HttpClient;
import com.hsj.protocol.HttpServer;
import com.hsj.protocol.NettyClient;
import com.hsj.proxy.ProxyFactory;

import java.net.UnknownHostException;

//采用短连接，客户端一次请求之后就下线。
public class Consumer {
    public static void main(String[] args) throws UnknownHostException {
        //侵入式rpc调用
        Invocation invocation = new Invocation(
                HelloService.class.getName(),
                "sayHello",
                new Class[]{String.class},
                new Object[]{"yzdl"}
        );
        //  Tomcat+http
        //HttpClient httpclient = new HttpClient();
        //String result = httpclient.send("localhost",8085,invocation);

        //  Netty+tcp
        //NettyClient nettyClient = new NettyClient();
        //String result = nettyClient.send("localhost",8085,invocation);

        //Tomcat+http使用动态代理进行优化-非侵入式
        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String result = helloService.sayHello("yzdl");
        result = helloService.sayHello("yzdl");
        System.out.println(result);
    }
}
