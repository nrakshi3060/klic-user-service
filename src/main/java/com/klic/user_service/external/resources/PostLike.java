package com.klic.user_service.external.resources;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "POST_LIKES", schema = "klic")
@Setter
@Getter
public class PostLike {

    @Id
    @Column(name = "ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "IS_LIKE")
    private Boolean isLike = true;

    @Column(name = "CREATE_DATE_TIME")
    private OffsetDateTime createDateTime;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        this.createDateTime = OffsetDateTime.now();
    }
}
