package com.klic.user_service.external.resources;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "MEDIA", schema = "klic")
@Setter
@Getter
public class Media {

    @Id
    @Column(name = "ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "MEDIA_PATH", nullable = false, columnDefinition = "TEXT")
    private String mediaPath;

    @Column(name = "TAKEN_LOCATION", columnDefinition = "geography(Point, 4326)")
    private Point takenLocation;

    @Column(name = "UPLOADED_LOCATION", columnDefinition = "geography(Point, 4326)")
    private Point uploadedLocation;

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
