package com.hsj.proxy;

import com.hsj.HelloService;
import com.hsj.common.Invocation;
import com.hsj.dto.RpcConfig;
import com.hsj.factoryStrategy.ClientStrategyFactory;
import com.hsj.handlerStrategy.ClientStrategy;
import com.hsj.protocol.HttpClient;
import com.hsj.register.RedisServiceDiscovery;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.UnknownHostException;
import java.util.Map;

public class ProxyFactory {
    //传入一个目标类，获得目标类的代理类
    public static <T> T getProxy(Class<T> interfaceClass) throws UnknownHostException {
        RedisServiceDiscovery discovery = new RedisServiceDiscovery();

        //todo:根据用户配置选择JDK Proxy或是CGLIB
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader()
                , new Class[]{interfaceClass}
                , new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String address = discovery.discover(interfaceClass.getName());
                        String[] parts = address.split(":");
                        String host = parts[0];
                        Integer port = Integer.valueOf(parts[1]);
                        Invocation invocation = new Invocation(
                                interfaceClass.getName(),
                                method.getName(),
                                method.getParameterTypes(),
                                args
                        );
                        //读取.yml文件，并且使用策略模式选择协议类型
                        RpcConfig rpcConfig = new RpcConfig();
                        try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("rpcSettings.yml"))
                        {
                            //try-with-resources保证资源释放
                            Yaml yaml = new Yaml();
                            Map<String, Object> data = yaml.load(input);
                            rpcConfig.setProtocol((String) data.get("connect_type"));
                            //rpcConfig.setHostname((String) data.get("hostname"));
                            //rpcConfig.setPort((Integer) data.get("port"));
                            rpcConfig.setHostname(host); //从服务注册与发现中心获取
                            rpcConfig.setPort(port); //从服务注册与发现中心获取
                        }
                        ClientStrategy clientStrategy = ClientStrategyFactory.getStrategy(rpcConfig.getProtocol());
                        return clientStrategy.send(rpcConfig.getHostname(),rpcConfig.getPort(), invocation);
                    }
                });
        return (T) proxyInstance;
    }
}
