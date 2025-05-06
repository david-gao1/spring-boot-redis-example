package com.practise;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Publisher {

    private final JedisPool jedisPool;

    public Publisher(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void publish(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            System.out.println("Publishing to [" + channel + "]: " + message);
            jedis.publish(channel, message);
        } catch (Exception e) {
            System.err.println("Publisher error: " + e.getMessage());
        }
    }
}
