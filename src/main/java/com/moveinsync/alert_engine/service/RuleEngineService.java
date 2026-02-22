package com.moveinsync.alert_engine.service;

import com.moveinsync.alert_engine.config.RuleConfig;
import com.moveinsync.alert_engine.entity.*;
import com.moveinsync.alert_engine.repository.AlertEventRepository;
import com.moveinsync.alert_engine.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class RuleEngineService {

    private final RuleConfig ruleConfig;
    private final AlertRepository alertRepo;
    private final AlertEventRepository eventRepo;

    public RuleEngineService(
            RuleConfig ruleConfig,
            AlertRepository alertRepo,
            AlertEventRepository eventRepo
    ) {
        this.ruleConfig = ruleConfig;
        this.alertRepo = alertRepo;
        this.eventRepo = eventRepo;
    }

    public void evaluate(Alert alert) {

        Map<String, Object> rule =
                ruleConfig.getRules()
                        .get(alert.getSourceType());

        if (rule == null) return;

        if (rule.containsKey("escalate_if_count")) {

            int count =
                    ((Number) rule.get("escalate_if_count")).intValue();

            int window =
                    ((Number) rule.get("window_mins")).intValue();

            LocalDateTime since =
                    LocalDateTime.now().minusMinutes(window);

            // Get driverId from metadata
            String driverId =
                    alert.getMetadata().get("driverId");

            if (driverId == null) return;

            // Count only alerts for same driver + type
            int existing = alertRepo
                    .findBySourceTypeAndTimestampAfter(
                            alert.getSourceType(), since)
                    .stream()
                    .filter(a ->
                            driverId.equals(
                                    a.getMetadata().get("driverId")))
                    .toList()
                    .size();

            // Escalate only when threshold exactly met
            if (existing == count &&
                    alert.getStatus() == AlertStatus.OPEN) {

                alert.setSeverity(Severity.CRITICAL);
                alert.setStatus(AlertStatus.ESCALATED);

                alertRepo.save(alert);

                saveEvent(
                        alert.getAlertId(),
                        "ESCALATED",
                        "Threshold reached: " + count
                );
            }
        }
    }
    private void saveEvent(
            String alertId,
            String type,
            String reason
    ) {

        AlertEvent e =
                AlertEvent.builder()
                        .alertId(alertId)
                        .eventType(type)
                        .timestamp(LocalDateTime.now())
                        .reason(reason)
                        .build();

        eventRepo.save(e);
    }
}