package com.eazy.library.book;
import com.eazy.library.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path ="api/v1/book")
public class BookController {
    private  final BookService service;
    private  final UserService userService;

    @PreAuthorize("hasAnyAuthority('User','Admin')")
    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Book> getAllBooks(){
        return service.getAllBooks();
    }

    @GetMapping("/subscribe/{bookId}")
    public void borrowBook(@PathVariable Long bookId, @RequestParam("userId") Long userId){
        userService.addSubscription(bookId,userId);
    }

    @GetMapping("/unsubscribe/{bookId}")
    public void returnBook(@PathVariable Long bookId, @RequestParam("userId") Long userId){
        userService.removeSubscription(bookId,userId);
    }
}
