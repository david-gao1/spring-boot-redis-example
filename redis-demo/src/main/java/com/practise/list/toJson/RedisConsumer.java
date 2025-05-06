package com.practise.list.toJson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class RedisConsumer {

    private JedisPool jedisPool;
    private Thread consumerThread;

    public RedisConsumer(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    // 启动消费者线程
    public void start() {
        consumerThread = new Thread(this::consume);
        consumerThread.start();
    }

    // 消费数据并处理
    private void consume() {
        try (Jedis jedis = jedisPool.getResource()) {
            while (true) {  // 持续阻塞，直到有消息
                List<String> result = jedis.blpop(0, "chat-message-list");  // 阻塞直到有消息
                //result格式是这样，[ <key>, <value> ]
                if (result != null && result.size() == 2) {
                    String messageJson = result.get(1);
                    // 使用 FastJSON 反序列化为 List<ChatMessage> 对象
                    try {
                        List<ChatMessage> messages = JsonUtil.deserialize(messageJson);
                        for (ChatMessage message : messages) {
                            System.out.println("Consumed message: " + message);
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to deserialize message: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in consuming messages: " + e.getMessage());
        }
    }

    // 停止消费者线程
    public void stop() {
        if (consumerThread != null && consumerThread.isAlive()) {
            consumerThread.interrupt();  // 中断线程
        }
    }
}
