package com.a1b.dw.cloudinary.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.Optional;

@Component
public class AuthServiceClient {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceClient.class);

    private final RestClient restClient;

    public AuthServiceClient(
            @Value("${auth-service.base-url:https://ld-authorization-service-e47c3397469c.herokuapp.com}") String baseUrl,
            @Value("${auth-service.connect-timeout-ms:3000}") int connectTimeoutMs,
            @Value("${auth-service.read-timeout-ms:5000}") int readTimeoutMs) {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeoutMs);
        factory.setReadTimeout(readTimeoutMs);

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @SuppressWarnings("unchecked")
    public Optional<ValidatedUser> validate(String token) {
        try {
            Map<String, String> body = restClient.post()
                    .uri("/auth/validate")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, resp) -> {
                        throw new RestClientException("Invalid token: " + resp.getStatusCode());
                    })
                    .body(Map.class);

            if (body == null || body.get("userId") == null) {
                return Optional.empty();
            }

            return Optional.of(new ValidatedUser(
                    body.get("userId"),
                    body.get("mobile"),
                    body.getOrDefault("role", "USER")
            ));

        } catch (RestClientException ex) {
            log.debug("Auth service rejected token: {}", ex.getMessage());
            return Optional.empty();
        } catch (Exception ex) {
            log.warn("Auth service unreachable ({}): {}", ex.getClass().getSimpleName(), ex.getMessage());
            return Optional.empty();
        }
    }

    public record ValidatedUser(String userId, String mobile, String role) {}
}
