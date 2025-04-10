package com.hsj.handlerStrategy;

import com.hsj.common.Invocation;

public interface ClientStrategy {
    String send(String hostname, Integer port, Invocation invocation);
}
