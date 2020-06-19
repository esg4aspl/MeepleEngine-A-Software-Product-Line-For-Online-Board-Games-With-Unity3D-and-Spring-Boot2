package com.meepleengine.meepleserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("_monitoring/health")
public class HealthController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String health() {
        return "OK";
    }

}
