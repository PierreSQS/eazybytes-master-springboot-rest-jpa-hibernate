package com.eazybytes.eazyschool.config;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class JpaAuditConfig {
}
