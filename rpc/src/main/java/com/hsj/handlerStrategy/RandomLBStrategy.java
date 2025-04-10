package com.hsj.handlerStrategy;

import com.hsj.factoryStrategy.LBStrategyFactory;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class RandomLBStrategy implements LBStrategy {
    private final Random random = new Random();

    @Override
    public String LoadBalance(Set<String> aliveAddresses,String ClientIp) {
        if (aliveAddresses == null || aliveAddresses.isEmpty()) {
            throw new RuntimeException("No alive addresses available.");
        }
        ArrayList<String> list = new ArrayList<>(aliveAddresses);
        return list.get(random.nextInt(list.size()));
    }
}
