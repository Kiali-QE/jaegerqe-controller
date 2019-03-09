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
        try {
            logger.error("Connection lost to the broker [{}], wait 10 seconds and reconnect",
                    MqttUtils.config().getHostUrl());
            Thread.sleep(1000 * 10);
            MqttUtils.connect();
        } catch (Exception ex) {
            logger.error("Exception,", ex);
        }
    }

    @SuppressWarnings("unchecked")
    public void messageArrived(String topic, MqttMessage message) {
        try {
            logger.info("Message received: [topic:{}, qos:{}, payloadLength:{}]",
                    topic, message.getQos(), message.getPayload().length);
            if (topic.startsWith(MqttUtils.TOPIC_ABOUT_REPORTER)) {
                Map<String, Object> data = (Map<String, Object>) SerializationUtils.deserialize(message.getPayload());
                ReporterNodeRepo.updateReporters(data);
            } else {
                logger.info("No action performed");
            }
        } catch (Exception ex) {
            logger.error("Exception,", ex);
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            StringBuilder topics = new StringBuilder();
            for (String topic : token.getTopics()) {
                topics.append(topic).append(", ");
            }
            logger.debug("delivery complete. [topics:{}]", topics);
        } catch (Exception ex) {
            logger.error("Exception,", ex);
        }
    }

}
