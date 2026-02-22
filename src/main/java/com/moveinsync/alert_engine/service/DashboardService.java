package com.moveinsync.alert_engine.service;

import com.moveinsync.alert_engine.entity.Alert;
import com.moveinsync.alert_engine.entity.AlertStatus;
import com.moveinsync.alert_engine.entity.Severity;
import com.moveinsync.alert_engine.repository.AlertEventRepository;
import com.moveinsync.alert_engine.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AlertRepository alertRepo;
    private final AlertEventRepository eventRepo;

    public Map<String, Object> dashboardSummary() {

        List<Alert> alerts = alertRepo.findAll();

        // Severity counts
        Map<Severity, Long> severityCounts =
                alerts.stream()
                        .collect(Collectors.groupingBy(
                                Alert::getSeverity,
                                Collectors.counting()
                        ));

        // Top 5 drivers (OPEN + ESCALATED)
        Map<String, Long> driverCounts =
                alerts.stream()
                        .filter(a ->
                                a.getStatus() == AlertStatus.OPEN ||
                                        a.getStatus() == AlertStatus.ESCALATED)
                        .filter(a -> a.getMetadata().containsKey("driverId"))
                        .collect(Collectors.groupingBy(
                                a -> a.getMetadata().get("driverId"),
                                Collectors.counting()
                        ));

        List<Map.Entry<String, Long>> topDrivers =
                driverCounts.entrySet()
                        .stream()
                        .sorted((a, b) ->
                                Long.compare(b.getValue(), a.getValue()))
                        .limit(5)
                        .toList();

        return Map.of(
                "severityCounts", severityCounts,
                "topDrivers", topDrivers,
                "recentEvents",
                eventRepo.findTop10ByOrderByTimestampDesc()
        );
    }

    // Trend over time (daily)
    public Map<LocalDate, Long> trend() {

        return alertRepo.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        a -> a.getTimestamp().toLocalDate(),
                        Collectors.counting()
                ));
    }
}