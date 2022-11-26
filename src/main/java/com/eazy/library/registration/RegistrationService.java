package com.eazy.library.registration;

import com.eazy.library.exceptions.ApiRequestException;
import com.eazy.library.exceptions.ResourceNotFoundException;
import com.eazy.library.helper.Helper;
import com.eazy.library.token.ConfirmationToken;
import com.eazy.library.token.ConfirmationTokenService;
import com.eazy.library.user.UserAccount;
import com.eazy.library.user.UserRole;
import com.eazy.library.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {
    private final UserService userService;
    private final ConfirmationTokenService tokenService;
    private final Helper helper;

    public String register(RegistrationBody body) {

        String token = userService.registerAccount(
                new UserAccount(
                        body.getFirstName(),
                        body.getLastName(),
                        body.getEmail(),
                        body.getPassword(),
                        UserRole.User
                )
        );
        helper.sendConfirmationMail(body.getEmail(), body.getFirstName(), token);
        return token;
    }


    public void registerAdmin(RegistrationBody body) {
        final UserAccount admin = new UserAccount(
                body.getFirstName(),
                body.getLastName(),
                body.getEmail(),
                body.getPassword(),
                UserRole.Admin
        );
        userService.registerAdmin(admin);
    }

    public String confirmToken(String token) {
        log.info("confirming token");
        final ConfirmationToken confirmationToken =
                tokenService.getToken(token)
                        .orElseThrow(() -> new ResourceNotFoundException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            log.error("user confirmed");
            throw new ApiRequestException("email already confirmed");
        }
        final LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            log.error("token expired");
            throw new ApiRequestException("token expired");
        }

        tokenService.setConfirmedAt(token);
        userService.enableUserAccount(confirmationToken.getUser().getEmail());
        return "confirmed";
    }

}
