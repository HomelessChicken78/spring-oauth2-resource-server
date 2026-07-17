package it.itsacademy.springoauth2resourceserver.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) return Optional.of("UNKNOWN_USER");

        return Optional.ofNullable(authentication.getName());
    }
}