package com.example.podscaling;

import com.example.podscaling.metrics.CustomMeterRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Endpoint(id = "avgRequests")
@Component
@Slf4j
public class AvgRequestsEndpoint {
    private final CustomMeterRegistry meterRegistry;

    public AvgRequestsEndpoint(CustomMeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @ReadOperation
    public Map<String, Double> getMetrics() throws JsonProcessingException {
        log.info("Requesting average requests: " + new ObjectMapper().writeValueAsString(meterRegistry.getCurrentValues()));
        return meterRegistry.getCurrentValues();
    }
}
