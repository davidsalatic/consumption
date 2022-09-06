package com.energy.consumption.model;

public interface EntityWithPublicId {
    String getId();

    String getPublicId();

    void setPublicId(String publicId);
}
