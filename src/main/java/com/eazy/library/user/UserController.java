package com.eazy.library.user;

import com.eazy.library.registration.RegistrationBody;
import com.eazy.library.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(path = "management/api/v1/user")
@PreAuthorize("hasAuthority('Admin')")
public class UserController {

    final UserService service;
    final RegistrationService registrationService;


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addAdmin(@Valid @RequestBody RegistrationBody body){
        registrationService.registerAdmin(body);
    }
}
