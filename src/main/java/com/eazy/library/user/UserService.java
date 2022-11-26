package com.eazy.library.user;

import com.eazy.library.book.Book;
import com.eazy.library.book.BookRepository;
import com.eazy.library.exceptions.ApiRequestException;
import com.eazy.library.exceptions.ResourceExistsException;
import com.eazy.library.exceptions.ResourceNotFoundException;
import com.eazy.library.helper.Helper;
import com.eazy.library.subscription.Subscription;
import com.eazy.library.subscription.SubscriptionId;
import com.eazy.library.subscription.SubscriptionRepository;
import com.eazy.library.token.ConfirmationToken;
import com.eazy.library.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    private final BookRepository bookRepository;
    private final ConfirmationTokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    private final SubscriptionRepository subscriptionRepository;

    private final Helper helper;
    private final static String USER_NOT_FOUND = "user with email %s not found";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND, email))
                );
    }


    public String registerAccount(UserAccount account) {
        log.info("registering user");
        final boolean userExists =
                repository.findByEmail(account.getEmail()).isPresent();

        if (userExists) {
            final UserAccount userAccount = repository.findByEmail(account.getEmail()).get();
            if (!userAccount.isEnabled()) {
                log.info("sending confirmation token to user");
                sendNewToken(userAccount);
            }
            log.error("user already exists");
            throw new ApiRequestException("email already taken");
        }
        account.setPassword(encodePassword(account.getPassword()));
        repository.save(account);
        return createAndSaveToken(account);
    }

    public void registerAdmin(UserAccount account) {
        log.info("adding new admin");
        final boolean userExists =
                repository.findByEmail(account.getEmail()).isPresent();
        if (userExists) {
            log.error("user already exists");
            throw new ResourceExistsException("email already taken");
        }
        account.setPassword(encodePassword(account.getPassword()));
        account.setEnabled(true);
        repository.save(account);
    }

    public void addSubscription(Long bookId, Long userId) {
        log.info("adding subscription");
        final Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("book not found"));
        final UserAccount user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        final Subscription sub = new Subscription(
                new SubscriptionId(userId, bookId),
                user,
                book,
                LocalDateTime.now()
        );
        if (user.getSubscriptions().contains(sub)) {
            throw new ResourceExistsException("subscription already exists");
        }
        user.addSubscription(sub);
        repository.save(user);
    }

    public void removeSubscription(Long bookId, Long userId) {

        log.info("removing subscription");
        final UserAccount user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        final Subscription sub = subscriptionRepository.findById(new SubscriptionId(userId, bookId))
                .orElseThrow(() -> new ResourceNotFoundException("subscription not found"));
        user.removeSubscription(sub);
        repository.save(user);
    }


    private void sendNewToken(UserAccount account) {
        tokenService.updateExpiredAt(account.getId());
        final String token = createAndSaveToken(account);
        helper.sendConfirmationMail(account.getEmail(), account.getFirstName(), token);
        throw new ApiRequestException("verify your email");
    }

    public void enableUserAccount(String email) {
        repository.enableUser(email);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String createAndSaveToken(UserAccount account) {
        final String token = UUID.randomUUID().toString();
        final ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                account
        );
        tokenService.saveConfirmationToken(confirmationToken);
        return token;

    }
}
