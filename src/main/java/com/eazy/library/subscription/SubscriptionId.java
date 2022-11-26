package com.eazy.library.subscription;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SubscriptionId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "book_id")
    private Long bookId;
}
