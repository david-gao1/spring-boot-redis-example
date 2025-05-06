package com.practise.list.simple;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class ListReader {
    private final JedisPool jedisPool;
    private final String listKey;

    public ListReader(JedisPool jedisPool, String listKey) {
        this.jedisPool = jedisPool;
        this.listKey = listKey;
    }

    public void readBlocking() {
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                while (true) {
                    List<String> result = jedis.blpop(0, listKey); // 阻塞等待
                    if (result != null && result.size() == 2) {
                        System.out.println("Popped from list: " + result.get(1));
                    }
                }
            }
        }).start();
    }
}
