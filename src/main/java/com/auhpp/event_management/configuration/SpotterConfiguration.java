package com.auhpp.event_management.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class SpotterConfiguration {
    @Value("${app.spotter.base-url}")
    private String baseUrl;

    @Bean("spotterRestClient")
    public RestClient spotterRestClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder.baseUrl(baseUrl).build();
    }
}
