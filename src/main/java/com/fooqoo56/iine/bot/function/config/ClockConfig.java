package com.fooqoo56.iine.bot.function.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * LocalDateの時刻を制御する
 */
@Configuration
public class ClockConfig {

    /**
     * clockのbean定義
     *
     * @return Clock
     */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
