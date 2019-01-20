package com.example.shopifychallenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CollectsResponse {

    @SerializedName("collects")
    @Expose
    private List<Collect> collects = null;

    public List<Collect> getCollects() {
        return collects;
    }

    public void setCollects(List<Collect> collects) {
        this.collects = collects;
    }

}
