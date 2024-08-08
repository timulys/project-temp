package com.kep.portal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "deploy-test")
@RequestMapping("/api/v1/deploy-test")
@RestController
public class DeployTestController {


    @Tag(name = "deploy-test")
    @Operation(summary = "test-controller")
    @PostMapping
    public void deployTest() {


    }
}
