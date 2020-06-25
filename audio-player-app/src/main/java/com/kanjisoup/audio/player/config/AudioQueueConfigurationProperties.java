package com.kanjisoup.audio.player.config;

import com.kanjisoup.audio.event.sdk.config.AudioQueueClientConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.kanjisoup.audio.queue")
@Data
public class AudioQueueConfigurationProperties implements AudioQueueClientConfig {
    private String exchange;
    private String exchangeType;
    private String routingKey;
    private String queueName;
}
