package com.kanjisoup.config;

import com.kanjisoup.audio.player.AudioQueueConsumer;
import com.kanjisoup.audio.player.config.AudioQueueConfigurationProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "com.kanjisoup.audio.event.sdk",
    "com.kanjisoup.audio.player"
})
public class PlayerApplicationConfig {

  @Bean
  Queue queue(AudioQueueConfigurationProperties props) {
    return new Queue(props.getRoutingKey(), false);
  }

  @Bean
  FanoutExchange exchange(AudioQueueConfigurationProperties props) {
    return new FanoutExchange(props.getExchange());
  }

  @Bean
  Binding binding(Queue queue, FanoutExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange);
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter, AudioQueueConfigurationProperties props) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(props.getRoutingKey());
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(AudioQueueConsumer consumer) {
    return new MessageListenerAdapter(consumer, "receiveMessage");
  }
}
