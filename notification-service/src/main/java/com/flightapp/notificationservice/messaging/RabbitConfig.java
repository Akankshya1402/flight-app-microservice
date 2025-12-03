package com.flightapp.notificationservice.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${booking.email.exchange}")
    private String exchangeName;

    @Value("${booking.email.routing-key}")
    private String routingKey;

    @Value("${booking.email.queue}")
    private String queueName;

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue bookingQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding bookingBinding(Queue bookingQueue, TopicExchange bookingExchange) {
        return BindingBuilder.bind(bookingQueue)
                             .to(bookingExchange)
                             .with(routingKey);
    }
}
