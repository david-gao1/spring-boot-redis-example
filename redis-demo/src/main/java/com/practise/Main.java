package com.practise;

import redis.clients.jedis.JedisPool;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String channel = "debug-channel";
        JedisPool jedisPool = new JedisPool("localhost", 6379);

        // 启动订阅者
        Subscriber subscriber = new Subscriber(jedisPool, channel);
        subscriber.subscribe();

        // 等待订阅器启动完毕
        Thread.sleep(1000);

        // 发布消息
        Publisher publisher = new Publisher(jedisPool);
        publisher.publish(channel, "Hello from Publisher!");

        // 再等几秒观察是否接收到
        Thread.sleep(3000);
        System.out.println("Main thread complete. You can exit now.");
    }
}
