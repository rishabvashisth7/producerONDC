package com.infy.bapondc.web.rest;

import com.infy.bapondc.service.KafkaConsumer;
import com.infy.bapondc.service.dto.Product;
import java.lang.reflect.Array;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class DemoController {

    @Autowired
    KafkaTemplate<String, Product> kafkaTemplate;

    @Autowired
    KafkaConsumer kafkaConsumer;

    private static final String TOPIC = "publishtopic";

    Map<String, String> requesidmap = new HashMap<>();

    @PostMapping("/publish")
    public String publishMessage(@RequestBody String item) {
        System.out.println("Running");
        String id = UUID.randomUUID().toString();
        Product product = new Product(item, id);
        HashMap<String, String> hshmap = new HashMap<>();
        requesidmap.put(id, item);
        kafkaConsumer.map.put(id, hshmap);
        kafkaTemplate.send(TOPIC, product);

        System.out.println(requesidmap);
        return "Your Reference id is :" + id + "\nPLease use it when selecting Shop";
    }

    @PostMapping("/select/{referenceid}")
    public void selection(@RequestBody String id, @PathVariable String referenceid) {
        //        System.out.println(kafkaConsumer.map);
        String uri = "http://localhost:8081/" + id;
        System.out.println("Request ID- Product -->" + requesidmap + referenceid);
        String prod = requesidmap.get(referenceid);
        System.out.println("PRODDD" + prod);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(uri, prod, String.class);
        System.out.println("Calling BPP id->" + id);
    }
}
