package com.eazybytes.eazyschool.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ProjectConfiguration {

    public static final String ADMIN_EAZYSCHOOL = "admin@eazyschool.com";
    public static final String ADMIN_PWD = "54321";

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(ADMIN_EAZYSCHOOL, ADMIN_PWD);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        return restTemplateBuilder
                .basicAuthentication(ADMIN_EAZYSCHOOL,ADMIN_PWD)
                .build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(ADMIN_EAZYSCHOOL,ADMIN_PWD))
                .build();
    }

}
