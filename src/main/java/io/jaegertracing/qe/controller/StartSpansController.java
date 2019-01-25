package io.jaegertracing.qe.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import io.jaegertracing.qe.controller.mqtt.MqttUtils;

@SpringBootApplication
public class StartSpansController {

    public static void main(String[] args) {
        SpringApplication.run(StartSpansController.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        MqttUtils.connect();
    }
}
