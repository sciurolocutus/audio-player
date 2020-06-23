package com.kanjisoup.audio.player.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.kanjisoup.audio.player")
@Data
public class AudioPlayerConfig {
    private String baseDir;
}
