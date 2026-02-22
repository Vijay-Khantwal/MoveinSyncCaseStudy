package com.moveinsync.alert_engine.service;

import com.moveinsync.alert_engine.entity.*;
import com.moveinsync.alert_engine.repository.AlertEventRepository;
import com.moveinsync.alert_engine.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ComplianceService {

    private final AlertRepository alertRepo;
    private final AlertEventRepository eventRepo;

    public void markDocumentRenewed(String alertId) {

        Alert alert = alertRepo.findById(alertId)
                .orElseThrow();

        if (!alert.getSourceType().equalsIgnoreCase("compliance"))
            return;

        alert.setStatus(AlertStatus.AUTO_CLOSED);
        alertRepo.save(alert);

        eventRepo.save(AlertEvent.builder()
                .alertId(alertId)
                .eventType("AUTO_CLOSED")
                .reason("Document renewed")
                .timestamp(LocalDateTime.now())
                .build());
    }
}