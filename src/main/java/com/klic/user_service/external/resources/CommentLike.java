package com.klic.user_service.external.resources;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "comment_likes")
@Setter
@Getter
public class CommentLike {

    @Id
    @Column(name = "ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMENT_ID", nullable = false)
    private PostComment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "IS_LIKE")
    private Boolean isLike = true;

    @Column(name = "CREATE_DATE_TIME")
    private OffsetDateTime createDateTime;

    @Column(name = "UPDATE_DATE_TIME")
    private OffsetDateTime updateDateTime;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        this.createDateTime = OffsetDateTime.now();
        this.updateDateTime = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDateTime = OffsetDateTime.now();
    }
}
