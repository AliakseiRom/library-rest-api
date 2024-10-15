package com.example.library_rest_api.mapper;

import com.example.library_rest_api.model.Book;
import com.example.library_rest_api.model.BookRequestDto;
import com.example.library_rest_api.model.BookResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "available", source = "available")
    BookResponseDto toResponse(Book book);


    @Mapping(target = "id", ignore = true)
    Book toEntity(BookRequestDto bookDto);
}
