package com.hsj.register;

import com.hsj.factoryStrategy.LBStrategyFactory;
import com.hsj.handlerStrategy.LBStrategy;
import org.yaml.snakeyaml.Yaml;
import redis.clients.jedis.JedisPooled;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//客户端用于发现服务的类
public class RedisServiceDiscovery {
    private final JedisPooled jedis = new JedisPooled("121.40.226.231", 6379, "user","123456");

    public String discover(String serviceName) throws RuntimeException, UnknownHostException {
        String serviceKey = "service:" + serviceName;

        // 获取所有注册的服务地址
        Set<String> addresses = jedis.smembers(serviceKey);

        // 过滤掉已过期的（key 不存在即为心跳失效）
        Set<String> aliveAddresses = addresses.stream()
                .filter(addr -> {
                    String key = "provider:" + serviceName + ":" + addr;
                    return jedis.exists(key); // 判断 key 是否仍存在
                })
                .collect(Collectors.toSet());

        if (aliveAddresses.isEmpty()) {
            throw new RuntimeException("No alive providers found for service: " + serviceName);
        }

        // 简单返回第一个，也可以用随机或轮询
        //todo:策略模式实现多种LB算法-
        String LB = "";
        try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("rpcSettings.yml"))
        {
            //try-with-resources保证资源释放
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);
            LB = (String) data.get("LB");
        }
        catch (Exception e){e.printStackTrace();}
        LBStrategy lbStrategy = LBStrategyFactory.getSrategy(LB);
        return lbStrategy.LoadBalance(aliveAddresses, InetAddress.getLocalHost().getHostAddress());
    }
}
