package com.moveinsync.alert_engine.service;

import com.moveinsync.alert_engine.entity.*;
import com.moveinsync.alert_engine.repository.AlertEventRepository;
import com.moveinsync.alert_engine.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AlertService {

    private final AlertRepository repo;
    private final AlertEventRepository eventRepo;
    private final RuleEngineService ruleEngine;

    public AlertService(
            AlertRepository repo,
            AlertEventRepository eventRepo,
            RuleEngineService ruleEngine
    ) {
        this.repo = repo;
        this.eventRepo = eventRepo;
        this.ruleEngine = ruleEngine;
    }

    public Alert create(Alert alert) {

        alert.setTimestamp(LocalDateTime.now());
        alert.setStatus(AlertStatus.OPEN);

        Alert saved = repo.save(alert);

        log(saved.getAlertId(),
                "CREATED",
                "Alert created");

        ruleEngine.evaluate(saved);

        return saved;
    }

    public Optional<Alert> resolve(String id) {

        Optional<Alert> a =
                repo.findById(id);

        a.ifPresent(alert -> {

            alert.setStatus(
                    AlertStatus.RESOLVED);

            repo.save(alert);

            log(id,
                    "RESOLVED",
                    "Manual resolve");
        });

        return a;
    }

    private void log(
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