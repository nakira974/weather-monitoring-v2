package com.example.weathermonitoringv2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SecurityService {
    private final RestTemplate restTemplate;

    @Autowired
    public SecurityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
