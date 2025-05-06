package com.practise.list.simple;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ListWriter {
    private final JedisPool jedisPool;
    private final String listKey;

    public ListWriter(JedisPool jedisPool, String listKey) {
        this.jedisPool = jedisPool;
        this.listKey = listKey;
    }

    public void write(String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(listKey, value); // 从右侧推入，模拟队列尾部
            System.out.println("Pushed to list: " + value);
        }
    }
}
