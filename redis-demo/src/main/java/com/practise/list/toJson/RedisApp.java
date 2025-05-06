package com.practise.list.toJson;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;

public class RedisApp {

    public static void main(String[] args) throws InterruptedException {

        // 创建 JedisPool 连接池
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);

        // 创建消费者并启动
        RedisConsumer consumer = new RedisConsumer(jedisPool);
        consumer.start();

        // 创建生产者
        RedisPublisher publisher = new RedisPublisher(jedisPool);

        // 模拟发布一些 List<ChatMessage> 消息
        ChatMessage message1 = new ChatMessage("Alice", "Hello, Bob!", System.currentTimeMillis());
        ChatMessage message2 = new ChatMessage("Bob", "Hi Alice!", System.currentTimeMillis());

        // 发布消息
        publisher.publishMessages("chat-message-list", Arrays.asList(message1, message2));

        // 稍等一会，查看 RedisConsumer 是否消费到消息
        Thread.sleep(5000);

        // 停止消费者
        consumer.stop();

        // 关闭连接池
        jedisPool.close();

        System.out.println("Main thread complete.");
    }
}
