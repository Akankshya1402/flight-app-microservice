package com.flightapp.bookingservice.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // ---- CONSTANTS USED BY BOTH SERVICES ----
    public static final String EMAIL_EXCHANGE = "booking.email.exchange";
    public static final String EMAIL_QUEUE = "booking.email.queue";
    public static final String EMAIL_ROUTING_KEY = "booking.email.routing-key";

    public static final String DLX = "booking.email.dlx";
    public static final String DLQ = "booking.email.dlq";
    public static final String DLQ_ROUTING_KEY = "booking.email.dlq.key";

    // ---- EXCHANGE ----
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    // ---- DLX ----
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX);
    }

    // ---- MAIN QUEUE ----
    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    // ---- DLQ ----
    @Bean
    public Queue emailDeadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    // ---- BINDINGS ----
    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(emailDeadLetterQueue())
                .to(deadLetterExchange())
                .with(DLQ_ROUTING_KEY);
    }

    // ---- JSON CONVERTER ----
    @Bean
    public Jackson2JsonMessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // ---- PRODUCER TEMPLATE ----
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    // ---- LISTENER FACTORY (consumer) ----
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(3);
        factory.setDefaultRequeueRejected(false);

        return factory;
    }
}


