package com.infy.bapondc.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.Map;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    public Map<String, Map<String, String>> map = new HashMap<>();

    @KafkaListener(topics = "newtopicresponse", groupId = "group_id")
    public void consume(String message) {
        //        System.out.println("Response from Consumer in producer= " + message);
        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
        String requesid = jsonObject.get("id").toString();
        requesid = requesid.substring(3, requesid.length() - 3);
        String s = (jsonObject.get("items").toString());
        s = s.substring(1, s.length() - 1);
        jsonObject = new JsonParser().parse(s).getAsJsonObject();

        map.get(requesid).put(jsonObject.get("id").toString(), jsonObject.get("name").toString());

        System.out.println(map);
    }
}
