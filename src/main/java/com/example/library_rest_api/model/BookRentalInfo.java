package com.example.library_rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "book_rental_info")
@Getter
@Setter
public class BookRentalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "rent_at", nullable = false)
    private LocalDate rentAt;

    @Column(name = "return_at", nullable = false)
    private LocalDate returnAt;
}
