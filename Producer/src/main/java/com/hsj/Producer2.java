package com.hsj;


import com.hsj.protocol.HttpServer;
import com.hsj.register.LocalRegister;
import com.hsj.register.RedisServiceRegister;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Producer2 {
    public static void main(String[] args) throws UnknownHostException {
        LocalRegister.regist(HelloService.class.getName(),HelloServiceImpl.class); //把服务注册到本地注册表中
        // 2. 注册到 Redis 注册中心
        RedisServiceRegister register = new RedisServiceRegister();
        //没有实现内网穿透
        //register.register(HelloService.class.getName(), InetAddress.getLocalHost().getHostAddress(), 8085);
        //使用localhost模拟
        register.register(HelloService.class.getName(), "localhost", 8086);

        //底层通过读取.yml+策略模式实现在Netty和Tomcat中切换
        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost",new Integer(8086));

    }
}