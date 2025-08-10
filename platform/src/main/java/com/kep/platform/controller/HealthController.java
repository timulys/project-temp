package com.kep.platform.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
public class HealthController {
    @GetMapping("/healthz")
    public ResponseEntity<String> healthz() { return ResponseEntity.ok("ok"); }
    @GetMapping("/readyz") public String ready() { return "ok"; }
}
