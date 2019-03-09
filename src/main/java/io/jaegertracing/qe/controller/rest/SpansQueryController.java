package io.jaegertracing.qe.controller.rest;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jaegertracing.qe.controller.mqtt.MqttUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/spansquery")
@Slf4j
public class SpansQueryController {
    private static final String DEFAULT_DESTINATION = MqttUtils.TOPIC_SPAN_QUERY;
    private static final String KEY_DESTINATION = "destination";
    private static final String KEY_JOB_ID = "jobId";

    @PostMapping("/trigger")
    public void triggerQuery(@RequestBody Map<String, Object> data) {
        logger.debug("Received a request. data:{}", data);
        String destination = DEFAULT_DESTINATION;
        if (data.get(KEY_DESTINATION) != null) {
            destination = (String) data.get(KEY_DESTINATION);
        }
        // update job id, if not available
        if (data.get(KEY_JOB_ID) == null) {
            data.put(KEY_JOB_ID, UUID.randomUUID().toString());
        }
        MqttUtils.publish(destination, data, 1);

    }
}