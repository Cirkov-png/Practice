package com.practice.lms.common.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("keycloak")
@RequiredArgsConstructor
public class CloudSecurityConfig {

    private final KeycloakRoleConverter keycloakRoleConverter;

    @Value("${security.manager.name}")
    private String managerName;

    @Value("${security.manager.password}")
    private String managerPassword;

    @Bean
    @Order(1)
    public SecurityFilterChain actuatorSecurityFilterChain(final HttpSecurity http) throws Exception {
        http
                .securityMatcher("/actuator/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().hasRole("MANAGER")
                )
                .httpBasic(org.springframework.security.config.Customizer.withDefaults());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/courses/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/courses/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/courses/**").hasRole("MANAGER")
                        .anyRequest().hasRole("USER")
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakRoleConverter))
                );

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager cloudUserDetailsManager() {
        final var encoder = new BCryptPasswordEncoder();
        final var manager = User.builder()
                .username(managerName)
                .password(encoder.encode(managerPassword))
                .roles("MANAGER")
                .build();

        return new InMemoryUserDetailsManager(manager);
    }
}
