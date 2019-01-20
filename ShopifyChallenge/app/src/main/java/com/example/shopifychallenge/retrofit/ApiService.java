package com.example.shopifychallenge.retrofit;

import com.example.shopifychallenge.model.CollectionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface ApiService {


    //Get all collections
    @GET("admin/custom_collections.json")
    Call<CollectionsResponse> getCollections(@Query("page") int page,
                                             @Query("access_token") String accessToken);


}
