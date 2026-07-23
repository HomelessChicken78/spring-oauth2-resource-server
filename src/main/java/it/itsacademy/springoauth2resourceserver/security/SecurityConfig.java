package it.itsacademy.springoauth2resourceserver.security;

import it.itsacademy.springoauth2resourceserver.model.User;
import it.itsacademy.springoauth2resourceserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpMethod.*;

@Configuration @EnableWebSecurity
public class SecurityConfig {
    @Value("${springdoc.api-docs.path}")
    private String apiDocsPath;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerUiPath;

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter(UserRepository repository) {
        return jwt -> {
            String sub = jwt.getSubject();

            Optional<User> optionalUser = repository.findBySub(sub);

            Collection<? extends GrantedAuthority> authorities = List.of();

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (!user.isActive()) throw new DisabledException("User account is disabled");

                authorities = user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                        .toList();
            }

            return new JwtAuthenticationToken(jwt, authorities);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter) throws Exception {
        return http.authorizeHttpRequests((auth) ->
                        auth.requestMatchers(apiDocsPath + "/**").permitAll()
                        .requestMatchers(swaggerUiPath + "/**").permitAll()
                        .requestMatchers(PUT, "/auth/signup").authenticated()
                        .requestMatchers(PATCH, "/auth/users/{nickname}/disable").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(PATCH, "/auth/users/{nickname}/enable").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(PATCH, "/auth/users/{nickname}/changeUserRoles").hasRole("ADMIN")
                        .requestMatchers(PUT, "/auth/users/**").hasRole("USER")
                        .requestMatchers("/posts/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .requestMatchers("/posts/{postId}/comments/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .anyRequest().hasAnyRole("ADMIN", "MANAGER", "USER")
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
        .build();
    }
}
