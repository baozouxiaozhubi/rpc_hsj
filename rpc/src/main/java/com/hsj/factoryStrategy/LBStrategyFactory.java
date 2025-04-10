package com.hsj.factoryStrategy;

import com.hsj.handlerStrategy.*;

import java.util.HashMap;
import java.util.Map;

public class LBStrategyFactory {
    private static final Map<String,LBStrategy> LBStrategymap = new HashMap<>();

    static
    {
        LBStrategymap.put("random",new RandomLBStrategy());
        LBStrategymap.put("Round Robin",new RoundRobinLBStrategy());
        LBStrategymap.put("Consistent Hash",new ConsistentHashLBStrategy());
        LBStrategymap.put("Least Connection",new LeastConnectionLBStrategy());
    }

    public static LBStrategy getSrategy(String strategyName)
    {
        return LBStrategymap.getOrDefault(strategyName
        ,LBStrategymap.get("random"));
    }
}
