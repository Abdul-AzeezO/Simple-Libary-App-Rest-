package com.eazy.library.book;
import com.eazy.library.subscription.Subscription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor

@Entity(name = "Book")
@Table(
        name = "book",
        uniqueConstraints = @UniqueConstraint(
                name = "book_uk",
                columnNames = "isbn"
        )
)
public class Book {
    @Id
    @SequenceGenerator(
            name = "book_sequence",
            sequenceName = "book_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "book_sequence"
    )
    @Column(name = "book_id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "isbn", nullable = false)
    private String isbn;
    @Column(name = "description", nullable  = false)
    private String description;
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToMany(
            mappedBy = "book",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private final List<Subscription> subscriptions = new ArrayList<>();

    public Book(String name, String isbn, String description, String imageUrl) {
        this.name = name;
        this.isbn = isbn;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(name, book.name) &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(description, book.description) &&
                Objects.equals(imageUrl, book.imageUrl);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isbn, description, imageUrl);
    }
}
