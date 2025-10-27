package com.klic.user_service.external.resources;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "post_comments")
@Setter
@Getter
public class PostComment {

    @Id
    @Column(name = "ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_COMMENT_ID")
    private PostComment parentComment;

    @Column(name = "COMMENT_TEXT", nullable = false, columnDefinition = "TEXT")
    private String commentText;

    @Column(name = "TOTAL_LIKES")
    private Integer totalLikes = 0;

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
