package com.eazy.library.subscription;

import com.eazy.library.book.Book;
import com.eazy.library.user.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "Subscription")
@Table(name = "subscription")
public class Subscription {
    @EmbeddedId
    private SubscriptionId id;

    @JsonIgnore
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "subscription_user_id_fk")
    )
    private UserAccount user;

    @JsonIgnore
    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(
            name = "book_id",
            foreignKey = @ForeignKey(name = "subscription_book_id_fk")
    )
    private Book book;

    @Column(
            name = "subscribed_on",
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private LocalDateTime subscribedOn;


    public Subscription(UserAccount user, Book book, LocalDateTime subscribedOn){
        this.user = user;
        this.book = book;
        this.subscribedOn  = subscribedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Subscription that = (Subscription) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
