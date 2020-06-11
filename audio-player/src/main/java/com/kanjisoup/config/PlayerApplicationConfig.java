package com.kanjisoup.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "com.kanjisoup.audio.event.sdk"
})
public class PlayerApplicationConfig {
}
