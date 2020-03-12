package com.example.podscaling.metrics;

import io.micrometer.core.instrument.step.StepRegistryConfig;

import java.time.Duration;

public interface CustomRegistryConfig extends StepRegistryConfig {

    CustomRegistryConfig DEFAULT = k -> null;

    @Override
    default String prefix() {
        return "custom";
    }

    @Override
    default Duration step() {
        String v = get(prefix() + ".step");
        return v == null ? Duration.ofSeconds(1) : Duration.parse(v);
    }

}
