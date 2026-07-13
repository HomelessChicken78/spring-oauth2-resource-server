package it.itsacademy.springoauth2resourceserver.dto;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class PageResponseDTO <T> {
    private T content;
    private Number currentPage;
    private Number totalPages;
    private Number totalElements;
    private Number pageSize;
}
