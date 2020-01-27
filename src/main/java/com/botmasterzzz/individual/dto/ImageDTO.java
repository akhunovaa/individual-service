package com.botmasterzzz.individual.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ImageDTO {

    @JsonIgnore
    private long id;

    private String imageUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
