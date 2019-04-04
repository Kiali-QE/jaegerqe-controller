package io.jaegertracing.qe.controller.handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jaegertracing.qe.controller.BeanUtil;

import io.jaegertracing.qe.controller.mqtt.MqttConf;
import io.jaegertracing.qe.controller.model.AboutMe;

@RestController
@RequestMapping("/api")
public class SystemHandler {

    @GetMapping("/status")
    public AboutMe startReporter() {
        return new AboutMe();
    }

    @GetMapping("/mqttconfig")
    public MqttConf getMqttConfig() {
        return BeanUtil.getBean(MqttConf.class);
    }
}