package com.energy.consumption.profile.model;

import com.energy.consumption.model.EntityWithPublicId;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "profile")
public class Profile implements EntityWithPublicId {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "public_id", nullable = false, unique = true)
    private String publicId;

    @PrePersist
    private void prePersist() {
        if (publicId != null) {
            publicId = publicId.trim();
        }
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
