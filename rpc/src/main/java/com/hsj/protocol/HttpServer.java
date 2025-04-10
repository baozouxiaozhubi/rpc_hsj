package com.hsj.protocol;

import com.hsj.factoryStrategy.ConnectStrategyFactory;
import com.hsj.handlerStrategy.ConnectStrategy;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class HttpServer {
    public void start(String hostname, Integer port){
        //前置工作:从用户的对应目录下的.properties或.yml文件或从Nacos()中读取配置项，选择是使用Netty还是Tomcat实现
        String type = get_connect_type();
        //if-else改为策略模式
        ConnectStrategy connectStrategy = ConnectStrategyFactory.getStrategy(type);
        connectStrategy.connect(hostname, port);
    }

    private String get_connect_type()
    {
        String type = null;
        //todo:添加.properties支持，并且加入判断配置文件是否存在/重复的逻辑
        //todo:改为从Nacos获取配置
        try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("rpcSettings.yml"))
        {
            //try-with-resources保证资源释放
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);
            type=(String) data.get("connect_type");
        }
        catch (IOException e) {e.printStackTrace();}
        return type;
    }
}
