package com.hsj.factoryStrategy;

import com.hsj.handlerStrategy.*;

import java.util.HashMap;
import java.util.Map;

public class ClientStrategyFactory {
    //根据配置文件选择Tomcat/Netty
    private static final Map<String, ClientStrategy> clientStrategyMap = new HashMap<>();

    static
    {
        clientStrategyMap.put("tomcat", new TomcatClientStrategy());
        clientStrategyMap.put("netty", new NettyClientStrategy());
    }

    public static ClientStrategy getStrategy(String strategyName)
    {
        return clientStrategyMap.getOrDefault(strategyName
                ,clientStrategyMap.get("tomcat"));
    }
}
