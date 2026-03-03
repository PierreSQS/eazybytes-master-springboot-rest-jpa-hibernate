package com.eazybytes.jobportal.dto;

import java.time.Instant;

// New to Sec16_Chap168
public record ContactResponseDto(
        Long id, String name, String email,
        String userType, String subject, String message,
        String status, Instant createdAt
) {
}
