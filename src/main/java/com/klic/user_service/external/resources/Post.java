package com.klic.user_service.external.resources;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "POSTS", schema = "klic")
@Setter
@Getter
public class Post {

    @Id
    @Column(name = "ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "POST_DESCRIPTION", nullable = false, columnDefinition = "TEXT")
    private String postDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "POST_TYPE", nullable = false, columnDefinition = "klic.POST_TYPE")
    
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private PostType postType;

    @Column(name = "TOTAL_LIKES")
    private Integer totalLikes = 0;

    @Column(name = "TOTAL_COMMENTS")
    private Integer totalComments = 0;

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
