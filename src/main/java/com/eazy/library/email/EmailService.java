package com.eazy.library.email;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService implements EmailSender {
    private final JavaMailSender mailSender;

    private Environment env;

    @Override
    public void send(String to, String email) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper messageHelper = new MimeMessageHelper(message, "utf-8");
            messageHelper.setText(email, true);
            messageHelper.setTo(to);
            messageHelper.setSubject("Confirm your email");
            messageHelper.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }

    }
}
