package com.moveinsync.alert_engine.controller;

import com.moveinsync.alert_engine.config.RuleConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

    private final RuleConfig ruleConfig;

    @GetMapping("/rules")
    public Object rules() {
        return ruleConfig.getRules();
    }
}