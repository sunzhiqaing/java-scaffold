package com.opc.scaffold.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 交换机/队列/绑定声明（direct 交换机演示）
 */
@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "scaffold.direct";
    public static final String QUEUE = "scaffold.queue";
    public static final String ROUTING_KEY = "scaffold.key";

    @Bean
    public DirectExchange scaffoldExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue scaffoldQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding scaffoldBinding(Queue scaffoldQueue, DirectExchange scaffoldExchange) {
        return BindingBuilder.bind(scaffoldQueue).to(scaffoldExchange).with(ROUTING_KEY);
    }
}
