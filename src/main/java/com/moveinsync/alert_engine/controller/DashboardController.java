package com.moveinsync.alert_engine.controller;

import com.moveinsync.alert_engine.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @GetMapping("/summary")
    public Object summary() {
        return service.dashboardSummary();
    }

    @GetMapping("/trends")
    public Object trends() {
        return service.trend();
    }
}