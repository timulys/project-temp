package com.kep.platform.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HealthController {
    @GetMapping("/healthz") public String health() { return "ok"; }
    @GetMapping("/readyz") public String ready() { return "ok"; }
}
