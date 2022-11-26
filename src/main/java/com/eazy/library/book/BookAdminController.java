package com.eazy.library.book;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(path = "management/api/v1/book")
@PreAuthorize("hasAuthority('Admin')")
public class BookAdminController {
    private final BookService service;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    void addBook(@Valid @RequestBody BookRequestBody body) {
        service.addBook(body);
    }

    @PutMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    void updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestBody body) {
         service.updateBook(id, body);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    void deleteBook(@PathVariable Long id) {
        service.deleteBook(id);
    }
}
