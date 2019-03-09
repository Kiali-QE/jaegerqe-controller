package io.jaegertracing.qe.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jaegertracing.qe.controller.model.AboutMe;

@RestController
@RequestMapping("/api/self")
public class Self {

    @GetMapping("/status")
    public AboutMe startReporter() {
        return new AboutMe();
    }
}