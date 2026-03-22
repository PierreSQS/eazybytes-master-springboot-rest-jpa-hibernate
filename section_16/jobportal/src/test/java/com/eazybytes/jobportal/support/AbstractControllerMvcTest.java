package com.eazybytes.jobportal.support;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

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

    protected String toJson(Object value) {
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
        SecretKey secretKey = new SecretKeySpec(
                ApplicationConstants.JWT_SECRET_DEFAULT_VALUE.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256");

        OctetSequenceKey jwk = new OctetSequenceKey.Builder(secretKey.getEncoded())
                .algorithm(JWSAlgorithm.HS256)
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        JwtEncoder encoder = new NimbusJwtEncoder(jwkSource);

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("jobportal-test")
                .subject("test-token")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("email", email)
                .claim("roles", roles)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    protected RestTestClient.RequestBodySpec withCsrf(RestTestClient.RequestBodySpec request) {
        return request.cookie(CSRF_COOKIE_NAME, CSRF_TOKEN)
                .header(CSRF_HEADER_NAME, CSRF_TOKEN);
    }
}
