package io.jaegertracing.qe.controller.reporter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class ReporterNodeRepo {

    private static final Map<String, ReporterNode> _DATA = new HashMap<>();

    public static synchronized void updateReporters(Map<String, Object> map) {
        ReporterNode node = ReporterNode.get(map);
        // check existing id from our local map
        ReporterNode localNode = _DATA.get(node.getHostname());
        if (localNode != null) {
            // if reference different than earlier, remove it
            if (localNode.getReference().equals(node.getReference())) {
                node.setId(localNode.getId());
            } else {
                IdGenerator.remove(localNode.getReference(), localNode.getId());
            }
        }
        // if id not set, generate it
        if (node.getId() == null) {
            node.setId(IdGenerator.generate(node.getReference()));
        }
        _DATA.put(node.getHostname(), node);
        logger.debug("Reporter data received: {}", node);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ReporterNode> getRepoStatus() {
        return (Map<String, ReporterNode>) ((HashMap<String, ReporterNode>) _DATA).clone();
    }

    @Scheduled(fixedRate = 45 * 1000L)
    private void removeDeadReporters() {
        // purge 40 seconds old reporter node details
        long purgeTimestamp = System.currentTimeMillis() - (40 * 1000L);
        for (String nodeName : _DATA.keySet()) {
            ReporterNode node = _DATA.get(nodeName);
            if (node != null) {
                if (node.getLastUpdate() <= purgeTimestamp) {
                    IdGenerator.remove(node.getReference(), node.getId());
                    _DATA.remove(nodeName);
                    logger.debug("Remove node: {}", node);
                }
            }
        }
    }

}
