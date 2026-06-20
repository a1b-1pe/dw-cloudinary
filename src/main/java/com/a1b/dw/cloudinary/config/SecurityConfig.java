package com.a1b.dw.cloudinary.config;

import com.a1b.dw.cloudinary.security.AuthServiceClient;
import com.a1b.dw.cloudinary.security.JwtAuthenticationFilter;
import com.a1b.dw.cloudinary.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${app.jwt.secret}") String jwtSecret) {
        return new JwtTokenProvider(jwtSecret);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            AuthServiceClient authServiceClient,
            JwtTokenProvider jwtTokenProvider,
            @Value("${auth.fallback.enabled:false}") boolean localFallbackEnabled,
            @Value("${auth.local-validation.enabled:false}") boolean localValidationEnabled) {
        return new JwtAuthenticationFilter(authServiceClient, jwtTokenProvider,
                localFallbackEnabled, localValidationEnabled);
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
            JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setEnabled(false);
        return reg;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
