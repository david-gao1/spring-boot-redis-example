package com.practise.list.toJson;


import com.alibaba.fastjson.JSON;

import java.util.List;

public class JsonUtil {

    // 序列化 List<ChatMessage> 为 JSON 字符串
    public static String serialize(List<ChatMessage> messages) {
        return JSON.toJSONString(messages);
    }

    // 反序列化 JSON 字符串为 List<ChatMessage>
    public static List<ChatMessage> deserialize(String json) {
        return JSON.parseArray(json, ChatMessage.class);
    }
}
