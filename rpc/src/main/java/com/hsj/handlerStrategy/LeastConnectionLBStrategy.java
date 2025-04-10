package com.hsj.handlerStrategy;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;

import java.util.Set;

public class LeastConnectionLBStrategy implements LBStrategy {
    private final JedisPooled jedis = new JedisPooled("121.40.226.231", 6379
            , "user","123456");

    @Override
    public String LoadBalance(Set<String> aliveAddresses, String serviceName) {
        if (aliveAddresses == null || aliveAddresses.isEmpty()) {
            throw new RuntimeException("No alive addresses available.");
        }

        String selectedAddress = null;
        int minConn = Integer.MAX_VALUE;

        for (String address : aliveAddresses) {
            String key = "loadbalance:" + serviceName + ":" + address;
            String value = jedis.get(key);
            int connCount = value == null ? 0 : Integer.parseInt(value);
            if (connCount < minConn) {
                minConn = connCount;
                selectedAddress = address;
            }
        }

        if (selectedAddress != null) {
            String selectedKey = "loadbalance:" + serviceName + ":" + selectedAddress;
            jedis.incr(selectedKey); // 增加连接数
        }

        return selectedAddress;
    }

    // 调用完成后调用此方法释放连接
    public void releaseConnection(String serviceName, String address) {
        String key = "loadbalance:" + serviceName + ":" + address;
        jedis.decr(key); // 减少连接数
    }
}
