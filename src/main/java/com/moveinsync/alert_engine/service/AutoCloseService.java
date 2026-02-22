package com.moveinsync.alert_engine.service;

import com.moveinsync.alert_engine.entity.Alert;
import com.moveinsync.alert_engine.entity.AlertEvent;
import com.moveinsync.alert_engine.entity.AlertStatus;
import com.moveinsync.alert_engine.repository.AlertEventRepository;
import com.moveinsync.alert_engine.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoCloseService {

    private final AlertRepository repository;
    private final AlertEventRepository eventRepository;

    @Value("${alert.expiry.minutes}")
    private int expiryMinutes;

    @Scheduled(fixedRate = 30000)
    public void autoCloseExpired() {

        List<Alert> alerts =
                repository.findByStatus(AlertStatus.OPEN);

        for (Alert alert : alerts) {

            if (alert.getTimestamp()
                    .isBefore(LocalDateTime.now().minusMinutes(expiryMinutes))) {

                alert.setStatus(AlertStatus.AUTO_CLOSED);
                repository.save(alert);

                eventRepository.save(AlertEvent.builder()
                        .alertId(alert.getAlertId())
                        .eventType("AUTO_CLOSED")
                        .reason("Time window expired")
                        .timestamp(LocalDateTime.now())
                        .build());
            }
        }
    }
}