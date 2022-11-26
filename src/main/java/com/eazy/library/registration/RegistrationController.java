package com.eazy.library.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService service;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public String register(@Valid @RequestBody RegistrationBody body) {
        return service.register(body);
    }

    @GetMapping(path = "confirm")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public String confirm(@RequestParam("token") String token) {
        return service.confirmToken(token);
    }
}
