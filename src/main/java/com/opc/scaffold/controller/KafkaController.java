package com.opc.scaffold.controller;

import com.opc.scaffold.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kafka 发送 + 监听演示
 */
@Tag(name = "Kafka 演示")
@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    public static final String TOPIC = "scaffold-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Operation(summary = "发送消息到 Kafka topic")
    @PostMapping("/send")
    public Result<String> send(@RequestParam(defaultValue = "scaffold-topic") String topic,
                               @RequestParam String msg) {
        kafkaTemplate.send(topic, msg);
        return Result.success("已发送到 " + topic + ": " + msg);
    }

    @KafkaListener(topics = TOPIC, groupId = "scaffold-group")
    public void listen(String message) {
        System.out.println(">>> [Kafka] 收到消息: " + message);
    }
}
