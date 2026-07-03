package it.itsacademy.springoauth2resourceserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.PUT;

@Configuration @EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((auth) ->
                        auth.requestMatchers(PUT, "/signup").permitAll()
                        .anyRequest().authenticated() // TODO Missing roles
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
        .build();
    }
}
