package com.kep.portal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "deploy-test")
@RestController("/api/v1/deploy-test")
public class DeployTestController {


    @Tag(name = "deploy-test")
    @PostMapping
    public void deployTest() {

    }
}
