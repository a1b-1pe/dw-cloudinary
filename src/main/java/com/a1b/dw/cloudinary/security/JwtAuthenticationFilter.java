package com.a1b.dw.cloudinary.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final AuthServiceClient authServiceClient;
    private final JwtTokenProvider  localJwtProvider;
    private final boolean           localFallbackEnabled;
    private final boolean           localValidationEnabled;

    public JwtAuthenticationFilter(AuthServiceClient authServiceClient,
                                    JwtTokenProvider localJwtProvider,
                                    boolean localFallbackEnabled,
                                    boolean localValidationEnabled) {
        this.authServiceClient      = authServiceClient;
        this.localJwtProvider       = localJwtProvider;
        this.localFallbackEnabled   = localFallbackEnabled;
        this.localValidationEnabled = localValidationEnabled;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null) {
            if (localValidationEnabled) {
                if (localJwtProvider.validateToken(token)) {
                    setAuthentication(
                            localJwtProvider.getUserId(token),
                            localJwtProvider.getRole(token));
                }
            } else {
                Optional<AuthServiceClient.ValidatedUser> validated = authServiceClient.validate(token);

                if (validated.isPresent()) {
                    AuthServiceClient.ValidatedUser user = validated.get();
                    setAuthentication(user.userId(), user.role() != null ? user.role() : "USER");

                } else if (localFallbackEnabled) {
                    log.warn("Auth service unavailable; using local JWT fallback");
                    if (localJwtProvider.validateToken(token)) {
                        setAuthentication(
                                localJwtProvider.getUserId(token),
                                localJwtProvider.getRole(token));
                    }
                } else {
                    log.warn("Auth service unavailable and local fallback disabled — request will be rejected");
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String userId, String role) {
        var auth = new UsernamePasswordAuthenticationToken(
                userId, null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
