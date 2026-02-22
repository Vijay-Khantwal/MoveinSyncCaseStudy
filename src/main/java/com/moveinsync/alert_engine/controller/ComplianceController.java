package com.moveinsync.alert_engine.controller;

import com.moveinsync.alert_engine.service.ComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compliance")
@RequiredArgsConstructor
public class ComplianceController {

    private final ComplianceService service;

    @PostMapping("/{alertId}/renewed")
    public void renew(@PathVariable String alertId) {
        service.markDocumentRenewed(alertId);
    }
}