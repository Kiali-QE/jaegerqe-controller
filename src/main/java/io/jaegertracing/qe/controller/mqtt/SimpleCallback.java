package io.jaegertracing.qe.controller.mqtt;

import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.util.SerializationUtils;

import io.jaegertracing.qe.controller.reporter.ReporterNodeRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleCallback implements MqttCallback {

    public void connectionLost(Throwable throwable) {
        logger.error("Connection lost to the broker [{}], wait 10 seconds and reconnect",
                MqttUtils.config().getHostUrl());
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException ex) {
            logger.error("Exception,", ex);
        }
        MqttUtils.connect();
    }

    @SuppressWarnings("unchecked")
    public void messageArrived(String topic, MqttMessage message) {
        logger.info("Message received: [topic:{}, payloadLength:{}, qos:{}]",
                topic, message.getPayload().length, message.getQos());
        if (topic.startsWith(MqttUtils.TOPIC_ABOUT_REPORTER)) {
            Map<String, Object> data = (Map<String, Object>) SerializationUtils.deserialize(message.getPayload());
            ReporterNodeRepo.updateReporters(data);
        } else {
            logger.info("No action performed");
        }

    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        StringBuilder topics = new StringBuilder();
        for (String topic : token.getTopics()) {
            topics.append(topic).append(", ");
        }
        logger.debug("delivery complete. [topics:{}]", topics);
    }

}
