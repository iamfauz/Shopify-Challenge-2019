package com.example.shopifychallenge.retrofit;

import com.example.shopifychallenge.model.CollectionsResponse;
import com.example.shopifychallenge.model.CollectsResponse;
import com.example.shopifychallenge.model.ProductsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface ApiService {


    //Get all collections
    @GET("admin/custom_collections.json")
    Call<CollectionsResponse> getCollections(@Query("page") int page,
                                             @Query("access_token") String accessToken);

    //Get all collects of a specific collection
    @GET("admin/collects.json")
    Call<CollectsResponse> getCollects(@Query("collection_id") String collectionId,
                                       @Query("page") int page,
                                       @Query("access_token") String accessToken);

    //Get all products of given product Id's
    @GET("admin/products.json")
    Call<ProductsResponse> getProducts(@Query("ids") String productIds,
                                       @Query("page") int page,
                                       @Query("access_token") String accessToken);

}
