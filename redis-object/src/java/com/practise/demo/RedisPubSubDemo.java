package com.practise.demo;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisPubSubDemo {

    private static final String CHANNEL_NAME = "debug-channel";

    public static void main(String[] args) throws InterruptedException {
        JedisPool jedisPool = new JedisPool("localhost", 6379);

        // 启动订阅线程
        Thread subscriberThread = new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                System.out.println("Subscribed to channel: " + CHANNEL_NAME);
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        System.out.println("Received message on channel [" + channel + "]: " + message);
                    }
                }, CHANNEL_NAME);
            } catch (Exception e) {
                System.err.println("Subscriber error: " + e.getMessage());
            }
        });

        subscriberThread.start();

        // 等待订阅线程初始化
        Thread.sleep(1000);

        // 发布消息
        try (Jedis jedis = jedisPool.getResource()) {
            System.out.println("Publishing message...");
            jedis.publish(CHANNEL_NAME, "Hello, Redis Pub/Sub!");
        }

        // 可选：等几秒后退出程序
        Thread.sleep(3000);
        System.out.println("Demo complete. You can terminate the app.");
    }
}
