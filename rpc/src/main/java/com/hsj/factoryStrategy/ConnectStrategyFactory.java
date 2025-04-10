package com.hsj.factoryStrategy;

import com.hsj.handlerStrategy.ConnectStrategy;
import com.hsj.handlerStrategy.NettyConnectStrategy;
import com.hsj.handlerStrategy.TomcatConnectStrategy;

import java.util.HashMap;
import java.util.Map;

//添加新的链接方式时只需要在handler中添加一个接口的实现类+在这里的Map多存一个策略
public class ConnectStrategyFactory {
    private static final Map<String, ConnectStrategy> connectStrategyMap = new HashMap<>();

    static
    {
        connectStrategyMap.put("tomcat", new TomcatConnectStrategy());
        connectStrategyMap.put("netty", new NettyConnectStrategy());
    }

    public static ConnectStrategy getStrategy(String strategyName)
    {
        return connectStrategyMap.getOrDefault(strategyName
                ,connectStrategyMap.get("tomcat"));
    }
}
