package com.hsj.handlerStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLBStrategy implements LBStrategy{
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public String LoadBalance(Set<String> aliveAddresses,String ClientIp) {
        if (aliveAddresses == null || aliveAddresses.isEmpty()) {
            throw new RuntimeException("No alive addresses available.");
        }
        List<String> list = new ArrayList<>(aliveAddresses);
        int i = Math.abs(index.getAndIncrement()) % list.size();
        return list.get(i);
    }
}
