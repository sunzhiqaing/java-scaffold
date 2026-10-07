package com.opc.scaffold.controller;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * RocketMQ 过滤消费者：selector=TagB，只收 TagB 标签消息
 */
@Component
@RocketMQMessageListener(topic = RocketMQController.TOPIC, consumerGroup = "scaffold-rmq-filter", selectorExpression = "TagB")
public class RocketMQFilterConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println(">>> [RocketMQ-Filter TagB] 收到: " + message);
    }
}
