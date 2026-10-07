package com.opc.scaffold.controller;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * RocketMQ 消费者：selector=*，收全部消息
 */
@Component
@RocketMQMessageListener(topic = RocketMQController.TOPIC, consumerGroup = "scaffold-rmq-group")
public class RocketMQConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println(">>> [RocketMQ] 收到(" + System.currentTimeMillis() + "): " + message);
    }
}
