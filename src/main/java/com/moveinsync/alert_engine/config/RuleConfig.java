package com.moveinsync.alert_engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.Map;

@Configuration
public class RuleConfig {

    private Map<String, Map<String, Object>> rules;

    public RuleConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("rules.json");

            if (is == null) {
                throw new RuntimeException("rules.json not found");
            }

            rules = mapper.readValue(is, Map.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load rules", e);
        }
    }

    public Map<String, Map<String, Object>> getRules() {
        return rules;
    }
}