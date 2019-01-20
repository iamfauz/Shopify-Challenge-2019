package com.example.shopifychallenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Collect {

    @SerializedName("id")
    @Expose
    private double id;
    @SerializedName("collection_id")
    @Expose
    private double collectionId;
    @SerializedName("product_id")
    @Expose
    private double productId;
    @SerializedName("featured")
    @Expose
    private Boolean featured;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("position")
    @Expose
    private double position;
    @SerializedName("sort_value")
    @Expose
    private String sortValue;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(double collectionId) {
        this.collectionId = collectionId;
    }

    public double getProductId() {
        return productId;
    }

    public void setProductId(double productId) {
        this.productId = productId;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public String getSortValue() {
        return sortValue;
    }

    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }

}