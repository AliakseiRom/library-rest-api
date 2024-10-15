package com.example.library_rest_api.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder(setterPrefix = "set", toBuilder = true)
public record BookRentalInfo(
        Long id,
        LocalDate rentAt,
        LocalDate returnAt
) {
}
