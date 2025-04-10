package com.hsj.handlerStrategy;

import java.util.Set;

public interface LBStrategy {
    String LoadBalance(Set<String> aliveAddresses,String ClientIp);
}
