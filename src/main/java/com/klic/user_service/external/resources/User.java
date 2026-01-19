package com.klic.user_service.external.resources;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "USERS", schema = "klic")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "SIGNUP_LOCATION", columnDefinition = "geography(Point, 4326)")
    private Point signupLocation;

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
