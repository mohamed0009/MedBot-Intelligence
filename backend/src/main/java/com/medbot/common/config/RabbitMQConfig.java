package com.medbot.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchanges
    public static final String DOCUMENT_EXCHANGE = "document.exchange";
    public static final String ANONYMIZATION_EXCHANGE = "anonymization.exchange";
    public static final String INDEXING_EXCHANGE = "indexing.exchange";

    // Queues
    public static final String DOCUMENT_PROCESSED_QUEUE = "document.processed";
    public static final String DOCUMENT_ANONYMIZED_QUEUE = "document.anonymized";
    public static final String DOCUMENT_INDEXED_QUEUE = "document.indexed";

    // Routing Keys
    public static final String DOCUMENT_PROCESSED_ROUTING_KEY = "document.processed";
    public static final String DOCUMENT_ANONYMIZED_ROUTING_KEY = "document.anonymized";
    public static final String DOCUMENT_INDEXED_ROUTING_KEY = "document.indexed";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    // Document Exchange and Queue
    @Bean
    public TopicExchange documentExchange() {
        return new TopicExchange(DOCUMENT_EXCHANGE);
    }

    @Bean
    public Queue documentProcessedQueue() {
        return QueueBuilder.durable(DOCUMENT_PROCESSED_QUEUE).build();
    }

    @Bean
    public Binding documentProcessedBinding() {
        return BindingBuilder
                .bind(documentProcessedQueue())
                .to(documentExchange())
                .with(DOCUMENT_PROCESSED_ROUTING_KEY);
    }

    // Anonymization Exchange and Queue
    @Bean
    public TopicExchange anonymizationExchange() {
        return new TopicExchange(ANONYMIZATION_EXCHANGE);
    }

    @Bean
    public Queue documentAnonymizedQueue() {
        return QueueBuilder.durable(DOCUMENT_ANONYMIZED_QUEUE).build();
    }

    @Bean
    public Binding documentAnonymizedBinding() {
        return BindingBuilder
                .bind(documentAnonymizedQueue())
                .to(anonymizationExchange())
                .with(DOCUMENT_ANONYMIZED_ROUTING_KEY);
    }

    // Indexing Exchange and Queue
    @Bean
    public TopicExchange indexingExchange() {
        return new TopicExchange(INDEXING_EXCHANGE);
    }

    @Bean
    public Queue documentIndexedQueue() {
        return QueueBuilder.durable(DOCUMENT_INDEXED_QUEUE).build();
    }

    @Bean
    public Binding documentIndexedBinding() {
        return BindingBuilder
                .bind(documentIndexedQueue())
                .to(indexingExchange())
                .with(DOCUMENT_INDEXED_ROUTING_KEY);
    }
}


