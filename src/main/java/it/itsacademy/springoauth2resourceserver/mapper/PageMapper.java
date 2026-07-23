package it.itsacademy.springoauth2resourceserver.mapper;

import it.itsacademy.springoauth2resourceserver.dto.common.PageResponseDTO;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.function.Function;

@Mapper(componentModel = "spring")
public interface PageMapper {
    default <T, R> PageResponseDTO<R> toDto(Page<T> page, Function<T, R> mapper) {
        return PageResponseDTO.<R>builder()
                .content((page.getContent().stream()
                        .map(mapper)
                        .toList()
                        ))
                .currentPage(page.getNumber() + 1) // Convert Spring Data's zero-based page index to a one-based API page number
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageSize(page.getSize())
                .build();
    }
}
