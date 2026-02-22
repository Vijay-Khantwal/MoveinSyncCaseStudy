package com.moveinsync.alert_engine.service;

import com.moveinsync.alert_engine.entity.AlertEvent;
import com.moveinsync.alert_engine.repository.AlertEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AlertEventRepository repo;

    public List<Map<String, Object>> dailyTrends() {

        List<AlertEvent> events = repo.findAllByOrderByTimestampAsc();

        Map<String, Map<String, Long>> grouped = new LinkedHashMap<>();

        for (AlertEvent e : events) {

            String date = e.getTimestamp()
                    .toLocalDate()
                    .toString();

            grouped.putIfAbsent(date, new HashMap<>());
            Map<String, Long> inner = grouped.get(date);

            inner.put(e.getEventType(),
                    inner.getOrDefault(e.getEventType(), 0L) + 1);
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (String date : grouped.keySet()) {

            Map<String, Object> row = new HashMap<>();
            row.put("date", date);
            row.put("escalated",
                    grouped.get(date).getOrDefault("ESCALATED", 0L));
            row.put("auto_closed",
                    grouped.get(date).getOrDefault("AUTO_CLOSED", 0L));
            row.put("resolved",
                    grouped.get(date).getOrDefault("RESOLVED", 0L));

            result.add(row);
        }

        return result;
    }
}