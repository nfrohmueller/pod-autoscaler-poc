package com.example.podscaling;

import com.example.podscaling.metrics.CustomMeterRegistry;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Endpoint(id = "avgRequests")
@Component
public class AvgRequestsEndpoint {
    private final CustomMeterRegistry meterRegistry;

    public AvgRequestsEndpoint(CustomMeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @ReadOperation
    public Map<String, Double> getMetrics() {
        return meterRegistry.getCurrentValues();
    }
}
