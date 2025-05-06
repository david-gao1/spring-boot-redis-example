package com.practise.list.simple;

import redis.clients.jedis.JedisPool;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        JedisPool jedisPool = new JedisPool("localhost", 6379);
        String listKey = "task-queue";

        // 启动读取线程（阻塞式读取）
        ListReader reader = new ListReader(jedisPool, listKey);
        reader.readBlocking();

        // 模拟写入任务
        ListWriter writer = new ListWriter(jedisPool, listKey);
        Thread.sleep(1000); // 等待读线程准备好
        writer.write("Task A");
        writer.write("Task B");

        Thread.sleep(5000); // 等待观察输出
    }
}
