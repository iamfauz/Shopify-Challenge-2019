package com.example.shopifychallenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Option {

    @SerializedName("id")
    @Expose
    private double id;
    @SerializedName("product_id")
    @Expose
    private double productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("position")
    @Expose
    private double position;
    @SerializedName("values")
    @Expose
    private List<String> values = null;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getProductId() {
        return productId;
    }

    public void setProductId(double productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

}