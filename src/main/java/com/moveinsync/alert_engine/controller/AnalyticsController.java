package com.moveinsync.alert_engine.controller;

import com.moveinsync.alert_engine.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService service;

    @GetMapping("/daily-trends")
    public List<Map<String, Object>> trends() {
        return service.dailyTrends();
    }
}