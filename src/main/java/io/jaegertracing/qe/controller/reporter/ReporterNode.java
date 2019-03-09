package io.jaegertracing.qe.controller.reporter;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.ToString;
import lombok.Data;

@Data
@ToString
@Builder
public class ReporterNode {
    private Integer id;
    private String hostname;
    private String reference;
    private Long lastUpdate;

    @JsonIgnore
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("hostname", hostname);
        map.put("lastUpdate", lastUpdate);
        return map;
    }

    public static ReporterNode get(Map<String, Object> map) {
        return ReporterNode.builder()
                .hostname((String) map.get("hostname"))
                .lastUpdate((Long) map.get("timestamp"))
                .reference((String) map.get("reference"))
                .build();
    }
}
