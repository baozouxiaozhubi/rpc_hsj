package com.hsj.handlerStrategy;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashLBStrategy implements LBStrategy{
    private final HashFunction hashFunction = Hashing.md5();
    private final int virtualNodeCount = 100; // 每个节点的虚拟副本数

    private final SortedMap<Integer, String> hashRing = new TreeMap<>();

    @Override
    public String LoadBalance(Set<String> aliveAddresses,String ClientIp) {
        if (aliveAddresses == null || aliveAddresses.isEmpty()) {
            throw new RuntimeException("No alive addresses available.");
        }

        // 重新构建一致性 Hash 环
        buildHashRing(aliveAddresses);

        // 示例 key：你可以用调用参数、用户 ID、IP 地址等,这里使用用户ID
        int hash = hashFunction.hashString(ClientIp, StandardCharsets.UTF_8).asInt();

        // 获取最近的节点
        SortedMap<Integer, String> tailMap = hashRing.tailMap(hash);
        Integer targetHash = tailMap.isEmpty() ? hashRing.firstKey() : tailMap.firstKey();
        return hashRing.get(targetHash);
    }

    private void buildHashRing(Set<String> nodes) {
        hashRing.clear();
        for (String node : nodes) {
            for (int i = 0; i < virtualNodeCount; i++) {
                String virtualNode = node + "##VN" + i;
                int hash = hashFunction.hashString(virtualNode, StandardCharsets.UTF_8).asInt();
                hashRing.put(hash, node);
            }
        }
    }
}
