package io.jaegertracing.qe.controller.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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

    public void messageArrived(String topic, MqttMessage message) {
        logger.info("Message received: [topic:{}, payload:{}, qos:{}]",
                topic, new String(message.getPayload()), message.getQos());

    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.debug("delivery complete. [topics:{}]", (Object[]) token.getTopics());
    }

}
