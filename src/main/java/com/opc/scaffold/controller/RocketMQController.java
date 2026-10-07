package com.opc.scaffold.controller;

import com.opc.scaffold.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RocketMQ 发送 + 监听演示（普通/延迟/顺序/过滤）
 */
@Tag(name = "RocketMQ 演示")
@RestController
@RequestMapping("/api/rocketmq")
public class RocketMQController {

    public static final String TOPIC = "scaffold-rmq";

    private final RocketMQTemplate rmt;

    public RocketMQController(RocketMQTemplate rmt) {
        this.rmt = rmt;
    }

    @Operation(summary = "发送 RocketMQ 消息（mode: normal/delay/order）")
    @PostMapping("/send")
    public Result<String> send(@RequestParam String msg,
                               @RequestParam(defaultValue = "normal") String mode,
                               @RequestParam(defaultValue = "TagA") String tag,
                               @RequestParam(defaultValue = "3") int delayLevel,
                               @RequestParam(defaultValue = "orderKey") String orderKey) {
        String dest = TOPIC + ":" + tag;
        switch (mode) {
            case "delay":
                // 延迟消息：delayLevel=3(10s) 后可见；API 为 syncSend(dest, Message, timeout, delayLevel)
                org.springframework.messaging.Message<String> rmqMsg =
                        org.springframework.messaging.support.MessageBuilder.withPayload(msg).build();
                rmt.syncSend(dest, rmqMsg, 3000L, delayLevel);
                break;
            case "order":
                // 顺序消息：同一 orderKey 路由同一队列，消费有序
                rmt.syncSendOrderly(dest, msg, orderKey);
                break;
            default:
                rmt.convertAndSend(dest, msg);
        }
        return Result.success("已发送 RocketMQ[" + mode + "|" + tag + "]: " + msg);
    }
}
