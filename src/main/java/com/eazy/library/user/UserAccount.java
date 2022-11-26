package com.eazy.library.user;

import com.eazy.library.subscription.Subscription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity(name = "UserAccount")
@Table(
        name = "user_account",
        uniqueConstraints = @UniqueConstraint(
                columnNames = "email",
                name = "user_email_unique"
        )
)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserAccount implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "user_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "user_id", updatable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @JsonIgnore
    @Column(name = "locked", nullable = false)
    private boolean locked = false;

    @JsonIgnore
    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    private final List<Subscription> subscriptions = new ArrayList<>();


    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    public void removeSubscription(Subscription subscription) {
        subscriptions.remove(subscription);
    }

    public UserAccount(String firstName, String lastName, String email, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return locked == that.locked &&
                enabled == that.enabled &&
                Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, role, locked, enabled);
    }
}
