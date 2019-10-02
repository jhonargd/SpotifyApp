package com.model;

import com.google.gson.annotations.SerializedName;

public class JsonPlayList {

    String name;
    String description;
    @SerializedName("public")
    private boolean publicText;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublicText() {
        return publicText;
    }

    public void setPublicText(boolean publicText) {
        this.publicText = publicText;
    }
}
