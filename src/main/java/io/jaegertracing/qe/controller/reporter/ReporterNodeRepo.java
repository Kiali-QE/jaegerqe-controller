package io.jaegertracing.qe.controller.reporter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.jaegertracing.qe.controller.mqtt.MqttUtils;

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
        boolean sendId = false;

        // get next free id and assign it, if smaller than assigned
        Integer nextAvailableId = IdGenerator.nextAvailableId(node.getReference());
        if (node.getId() != null) {
            if (node.getId() > nextAvailableId.intValue()) {
                IdGenerator.remove(node.getReference(), node.getId());
                node.setId(nextAvailableId);
                sendId = true;
            }
        } else {
            // check existing id from our local map
            ReporterNode localNode = _DATA.get(node.getHostname());
            if(localNode != null){
                IdGenerator.remove(localNode.getReference(), localNode.getId());
                _DATA.remove(node.getHostname());
            }
        }

        // if id not set, generate it
        if (node.getId() == null) {
            node.setId(IdGenerator.generate(node.getReference()));
            sendId = true;
        }
        _DATA.put(node.getHostname(), node);
        logger.debug("Reporter data received: {}", node);
        if (sendId) {
            MqttUtils.publish(MqttUtils.TOPIC_REPORTER_CONFIG, node.getMap(), 1);
        }
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
