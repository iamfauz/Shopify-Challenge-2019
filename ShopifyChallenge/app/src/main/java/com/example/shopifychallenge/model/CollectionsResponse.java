package com.example.shopifychallenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CollectionsResponse {

    @SerializedName("custom_collections")
    @Expose
    private List<CustomCollection> customCollections;

    public List<CustomCollection> getCustomCollections() {
        return customCollections;
    }

    public void setCustomCollections(List<CustomCollection> customCollections) {
        this.customCollections = customCollections;
    }

}