package com.energy.consumption.meter.model;

import com.energy.consumption.model.EntityWithPublicId;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "meter")
public class Meter implements EntityWithPublicId {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
