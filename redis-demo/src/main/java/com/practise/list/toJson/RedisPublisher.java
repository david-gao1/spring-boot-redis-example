package com.practise.list.toJson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class RedisPublisher {

    private JedisPool jedisPool;

    public RedisPublisher(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    // 发布消息
    public void publishMessages(String listKey, List<ChatMessage> messages) {
        try (Jedis jedis = jedisPool.getResource()) {
            String messageJson = JsonUtil.serialize(messages);  // 使用 FastJSON 序列化为 JSON
            jedis.rpush(listKey, messageJson);  // 推送到 Redis List
            System.out.println("Published message: " + messageJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
