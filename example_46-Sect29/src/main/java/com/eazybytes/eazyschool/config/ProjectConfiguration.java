package com.eazybytes.eazyschool.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfiguration {

    public static final String ADMIN_EAZYSCHOOL = "admin@eazyschool.com";
    public static final String ADMIN_PWD = "54321";

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(ADMIN_EAZYSCHOOL, ADMIN_PWD);
    }

}
