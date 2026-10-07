package com.opc.scaffold.controller;

import com.opc.scaffold.common.result.Result;
import com.opc.scaffold.config.RabbitConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RabbitMQ 发送 + 监听演示
 */
@Tag(name = "RabbitMQ 演示")
@RestController
@RequestMapping("/api/mq")
public class RabbitMQController {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Operation(summary = "发送消息到交换机（direct）")
    @PostMapping("/send")
    public Result<String> send(@RequestParam String msg) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, msg);
        return Result.success("已发送: " + msg);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void listen(String message) {
        System.out.println(">>> [RabbitMQ] 收到消息: " + message);
    }
}
