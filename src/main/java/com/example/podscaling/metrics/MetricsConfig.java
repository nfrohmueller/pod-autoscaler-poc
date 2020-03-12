package com.example.podscaling.metrics;

import io.micrometer.core.instrument.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public CustomRegistryConfig customRegistryConfig() {
        return CustomRegistryConfig.DEFAULT;
    }

    @Bean
    public CustomMeterRegistry customMeterRegistry(CustomRegistryConfig customRegistryConfig, Clock clock) {
        return new CustomMeterRegistry(customRegistryConfig, clock);
    }

}
