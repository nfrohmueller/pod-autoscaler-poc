package com.example.podscaling.metrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class CustomMeterRegistry extends StepMeterRegistry {
    private final ConcurrentMap<String, Double> currentValues = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final long stepSeconds;

    public CustomMeterRegistry(CustomRegistryConfig config, Clock clock) {
        super(config, clock);
        stepSeconds = config.step().toSeconds();
        start(new NamedThreadFactory("custom-metrics-publisher"));
    }

    @Override
    protected void publish() {
//        log.info("--------- Publisching custom metrics ----------");
        val collect = getMeters().stream()
                                 .filter(meter -> meter.getId()
                                                       .getName()
                                                       .equalsIgnoreCase("http.server.requests"))
                                 .filter(meter -> !meter.getId().getTag("uri").equalsIgnoreCase("/**"))
                                 .map(meter -> Pair.of(meter.getId()
                                                            .getTag("uri"), sum(meter)))
                                 .collect(groupingBy(Pair::getKey, averagingDouble(Pair::getValue)));

        currentValues.clear();
        if (!collect.isEmpty()) {
            currentValues.putAll(collect);
            try {
                log.info(mapper.writeValueAsString(collect));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private Double sum(Meter meter) {
        val sum = StreamSupport.stream(meter.measure()
                                         .spliterator(), false)
                            .mapToDouble(Measurement::getValue)
                            .sum();
        return sum / stepSeconds;
    }

    @Override
    protected TimeUnit getBaseTimeUnit() {
        return TimeUnit.SECONDS;
    }

    public Map<String, Double> getCurrentValues() {
        return currentValues;
    }

}
