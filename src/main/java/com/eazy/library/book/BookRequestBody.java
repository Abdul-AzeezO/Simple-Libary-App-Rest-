package com.eazy.library.book;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BookRequestBody {
    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "isbn is required")
    private String isbn;
    @NotNull(message = "description is required")
    private String description;
    @NotNull(message = "url is required")
    private String imageUrl;

}
