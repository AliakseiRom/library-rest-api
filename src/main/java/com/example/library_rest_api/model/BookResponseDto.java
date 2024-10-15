package com.example.library_rest_api.model;

import lombok.Builder;

@Builder(setterPrefix = "set", toBuilder = true)
public record BookResponseDto(
        Long id,
        String isbn,
        String title,
        String author,
        String description,
        String genre,
        Boolean available
){}
