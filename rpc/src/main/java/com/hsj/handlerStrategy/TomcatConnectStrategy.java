package com.hsj.handlerStrategy;

import com.hsj.protocol.DispatcherServlet;
import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;


public class TomcatConnectStrategy implements ConnectStrategy {
    @Override
    public void connect(String hostname, Integer port) {
        //代码配置创建一个嵌入式Tomcat实例，适合分布式微服务场景(每个服务需要一个Servlet容器)
        Tomcat tomcat =new Tomcat();

        //Tomcat 的最顶层容器，负责整个服务器生命周期管理。包含多个service
        Server server = tomcat.getServer();
        Service service =server.findService("Tomcat"); //Tomcat 启动时默认会创建一个名为 "Tomcat" 的 Service。

        //配置连接器，用于接受用户连接
        Connector connector = new Connector();
        connector.setPort(port); //监听器运行的接口

        //配置处理引擎，一个Service只能有一个Engine，功能是把请求分发给 Host（虚拟主机）
        //Engine 只调度本地的虚拟主机与应用结构，不具备将请求直接转发到远程 IP 的能力。
        Engine engine = new StandardEngine();
        engine.setDefaultHost(hostname);

        //虚拟主机，Tomcat 支持多个虚拟主机（比如根据域名分发服务），一个 Engine 下可以挂多个 Host。
        //这个 Host 负责管理 Web 应用（Context），相当于本地 localhost 域名的应用容器
        Host host = new StandardHost();
        host.setName(hostname);

        String contextPath = "";
        Context context = new StandardContext(); //创建一个新的 Context 实例，也就是一个Web 应用的容器。
        context.setPath(contextPath); //空字符串 "" 表示根路径，即请求 http://localhost:8080/ 就能访问上一行创建的context
        context.addLifecycleListener(new Tomcat.FixContextListener()); //生命周期监听器，做一些默认配置补充，比如设置默认的 WebXml、类加载器等。

        //组装容器
        host.addChild(context);
        engine.addChild(host);
        service.setContainer(engine);
        service.addConnector(connector);

        //添加处理器dispatchservlet 即实际处理请求的类
        tomcat.addServlet(contextPath,"dispatcher", new DispatcherServlet());
        context.addServletMappingDecoded("/*","dispatcher"); //将所有路径（/*） 的请求映射到名为 "dispatcher" 的 Servlet 上

        try
        {
            tomcat.start();
            tomcat.getServer().await();//阻塞当前线程，让服务持续运行
        }
        catch(LifecycleException e){e.printStackTrace();}
    }

}
