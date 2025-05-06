package com.practise;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class Subscriber {

    private final JedisPool jedisPool;
    private final String channel;

    public Subscriber(JedisPool jedisPool, String channel) {
        this.jedisPool = jedisPool;
        this.channel = channel;
    }

    public void subscribe() {
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                System.out.println("Subscribed to channel: " + channel);
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        System.out.println(">> Received on [" + channel + "]: " + message);
                        // 你可以在这里写自定义处理逻辑，比如保存数据库、触发事件等
                    }
                }, channel);
            } catch (Exception e) {
                System.err.println("Subscriber error: " + e.getMessage());
            }
        }).start();
    }
}
