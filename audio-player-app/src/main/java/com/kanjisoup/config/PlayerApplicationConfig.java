package com.kanjisoup.config;

import com.kanjisoup.audio.player.AudioQueueConsumer;
import com.kanjisoup.audio.player.config.AudioPlayerConfig;
import com.kanjisoup.audio.player.config.AudioQueueConfigurationProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "com.kanjisoup.audio.player"
})
@EnableConfigurationProperties({AudioQueueConfigurationProperties.class, AudioPlayerConfig.class})
public class PlayerApplicationConfig {

    @Bean
    Queue queue(AudioQueueConfigurationProperties props) {
        return new Queue(props.getQueueName(), false);
    }

    @Bean
    Exchange exchange(AudioQueueConfigurationProperties props) {
        return new ExchangeBuilder(props.getExchange(), props.getExchangeType())
            .build();
    }

    @Bean
    Binding binding(Queue queue, Exchange exchange, AudioQueueConfigurationProperties props) {
        return BindingBuilder.bind(queue)
            .to(exchange)
            .with(props.getRoutingKey())
            .noargs();
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
        AudioQueueConsumer consumer, AudioQueueConfigurationProperties props) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(props.getRoutingKey());
        container.setMessageListener(consumer);

        return container;
    }
}
