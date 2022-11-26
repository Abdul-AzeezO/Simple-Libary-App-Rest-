package com.eazy.library.registration;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegistrationBody {
    @NotEmpty(message = "first name is required")
    @Size(min = 2, max = 32, message = "name must be between 2 and 32 characters long")
    private String firstName;
    @NotEmpty(message = "last name is required")
    @Size(min = 2, max = 32, message = "name must be between 2 and 32 characters long")
    private String lastName;
    @NotEmpty(message = "email address is required")
    @Email(message = "The email address is invalid", regexp = "^(.+)@(.+)$")
    private String email;
    @NotEmpty(message = "password address is required")
    @Pattern(
            message = "password is invalid",
            regexp = "^([a-zA-Z0-9@*#]{8,15})$"
    )
    private String password;
}
