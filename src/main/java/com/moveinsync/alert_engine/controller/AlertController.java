package com.moveinsync.alert_engine.controller;

import com.moveinsync.alert_engine.entity.Alert;
import com.moveinsync.alert_engine.repository.AlertEventRepository;
import com.moveinsync.alert_engine.repository.AlertRepository;
import com.moveinsync.alert_engine.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService service;
    private final AlertRepository repo;
    private final AlertEventRepository eventRepo;

    public AlertController(
            AlertService service,
            AlertRepository repo,
            AlertEventRepository eventRepo
    ) {
        this.service = service;
        this.repo = repo;
        this.eventRepo = eventRepo;
    }

    @PostMapping
    public Alert create(@RequestBody Alert a) {
        return service.create(a);
    }

    @PostMapping("/{id}/resolve")
    public Object resolve(@PathVariable String id) {
        return service.resolve(id);
    }

    @GetMapping("/{id}")
    public Map<String, Object>
    drill(@PathVariable String id) {

        return Map.of(
                "alert", repo.findById(id),
                "events",
                eventRepo
                        .findByAlertIdOrderByTimestampAsc(id)
        );
    }

    @GetMapping("/auto-closed")
    public Object autoClosedLastHours(
            @RequestParam(defaultValue = "24") int hours
    ) {
        return eventRepo.findTop10ByOrderByTimestampDesc()
                .stream()
                .filter(e -> e.getEventType().equals("AUTO_CLOSED"))
                .toList();
    }
}