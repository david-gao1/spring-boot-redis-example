package com.practise.list.to3json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.SystemMessage;
import com.theokanning.openai.completion.chat.UserMessage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.List;



public class RedisChatDemo {

    private static final String LIST_KEY = "chat-message-list";

    public static void main(String[] args) throws Exception {
        JedisPool jedisPool = new JedisPool("localhost", 6379);
        ObjectMapper mapper = new ObjectMapper();

        // 创建消息
        List<ChatMessage> messages = Arrays.asList(
            new UserMessage("Hello from user"),
            new SystemMessage("System says hi")
        );

        // 序列化后写入 Redis
        try (Jedis jedis = jedisPool.getResource()) {
            String json = mapper.writeValueAsString(messages);
            jedis.rpush(LIST_KEY, json);
            System.out.println("Pushed to Redis: " + json);
        }


        Thread.sleep(1000);


        // 模拟消费（一次性 BLPOP）
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> result = jedis.blpop(5, LIST_KEY); // 最多等待5秒
            if (result != null && result.size() == 2) {
                String messageJson = result.get(1);
                List<ChatMessage> deserialized = mapper.readValue(
                    messageJson,
                    new TypeReference<List<ChatMessage>>() {}
                );

                for (ChatMessage msg : deserialized) {
                    System.out.printf("Received [%s]: %s%n", msg.getRole(), msg.getTextContent());
                }
            } else {
                System.out.println("No messages received.");
            }
        }

        jedisPool.close();
    }
}
