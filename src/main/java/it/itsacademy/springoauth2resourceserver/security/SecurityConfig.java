package it.itsacademy.springoauth2resourceserver.security;

import it.itsacademy.springoauth2resourceserver.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration @EnableWebSecurity
public class SecurityConfig {
    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter(UserRepository repository) {
        return jwt -> {
            String sub = jwt.getSubject();

            Collection<? extends GrantedAuthority> authorities = repository.findBySub(sub)
                    .map(user -> user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .toList()
                    )
                    .orElse(List.of());

            return new JwtAuthenticationToken(jwt, authorities);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter) throws Exception {
        return http.authorizeHttpRequests((auth) ->
                        auth.requestMatchers(PUT, "/auth/signup").authenticated()
                        .requestMatchers(PATCH, "/auth/users/{nickname}/disable").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(PATCH, "/auth/users/{nickname}/enable").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(PUT, "/auth/users/**").hasRole("USER")
                        .anyRequest().hasAuthority("ROLE_USER")
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
        .build();
    }
}
