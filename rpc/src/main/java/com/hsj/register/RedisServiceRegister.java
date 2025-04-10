package com.hsj.register;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RedisServiceRegister {
    private final JedisPooled jedis = new JedisPooled("121.40.226.231", 6379
            , "user","123456");
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void register(String serviceName, String host, int port) {
        String address = host + ":" + port;
        String key = "provider:" + serviceName + ":" + address;

        scheduler.scheduleAtFixedRate(() -> {
            jedis.setex(key, 10, "alive"); // 心跳机制：每 5 秒刷新 TTL = 10 秒
            jedis.sadd("service:" + serviceName, address); // 维护可用服务集合
        }, 0, 5, TimeUnit.SECONDS);
    }
}
