package io.jaegertracing.qe.controller.handler;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jaegertracing.qe.controller.mqtt.MqttUtils;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/mqtt")
@Slf4j
public class MqttHandler {
    private static final String MQTT_TOPIC = "mqttTopic";

    @PostMapping("/post")
    public void startReporter(@RequestBody Map<String, Object> data) {
        logger.debug("Received a request. data:{}", data);
        if (data.get(MQTT_TOPIC) != null) {
            MqttUtils.publish((String) data.get(MQTT_TOPIC), data, 1);
        }
    }
}