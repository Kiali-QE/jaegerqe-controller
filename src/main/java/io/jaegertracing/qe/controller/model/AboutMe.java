package io.jaegertracing.qe.controller.model;

import java.util.Collection;

import io.jaegertracing.qe.controller.reporter.ReporterNodeRepo;
import lombok.Getter;
import io.jaegertracing.qe.controller.reporter.ReporterNode;

@Getter
public class AboutMe {
    private Collection<ReporterNode> reporterNodes = ReporterNodeRepo.getRepoStatus();
    private Long timestamp = System.currentTimeMillis();
}
