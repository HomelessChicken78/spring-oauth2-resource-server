package it.itsacademy.springoauth2resourceserver.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        var ctx = SecurityContextHolder.getContext().getAuthentication();
        if (ctx == null) return Optional.of("UNKNOWN_USER");

        var principal = ctx.getPrincipal();
        if (principal == null) return Optional.of("UNKNOWN_USER");

        return Optional.of(principal.toString());
    }
}