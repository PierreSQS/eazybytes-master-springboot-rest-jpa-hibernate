package com.eazybytes.jobportal.support;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.MediaType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

public abstract class AbstractControllerMvcTest {

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;

    @MockitoBean(name = "auditorAwareImpl")
    private AuditorAware<String> auditorAware;

    protected static final MediaType JSON = MediaType.APPLICATION_JSON;
    protected static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    protected static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    protected static final String CSRF_TOKEN = "test-csrf-token";

    protected final JsonMapper jsonMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    protected String toJson(Object value) throws Exception {
        return jsonMapper.writeValueAsString(value);
    }

    protected String adminToken() {
        return createJwtToken("admin@jobportal.test", "ROLE_ADMIN");
    }

    protected String userToken() {
        return createJwtToken("user@jobportal.test", ApplicationConstants.ROLE_JOB_SEEKER);
    }

    protected String bearerToken(String token) {
        return "Bearer " + token;
    }

    protected String createJwtToken(String email, String roles) {
        SecretKey secretKey = Keys.hmacShaKeyFor(
                ApplicationConstants.JWT_SECRET_DEFAULT_VALUE.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer("jobportal-test")
                .subject("test-token")
                .claim("email", email)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))
                .signWith(secretKey)
                .compact();
    }

    protected RestTestClient.RequestBodySpec withCsrf(RestTestClient.RequestBodySpec request) {
        return request.cookie(CSRF_COOKIE_NAME, CSRF_TOKEN)
                .header(CSRF_HEADER_NAME, CSRF_TOKEN);
    }
}



