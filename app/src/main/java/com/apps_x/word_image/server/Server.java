package com.apps_x.word_image.server;

import com.apps_x.word_image.model.ResponseBodyModel;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Server {
    // Server domain
    String URL = "https://api.gettyimages.com";

    // Use when whe need get image from server
    @GET("/v3/search/images")
    Call<ResponseBodyModel> getImage(@Header("Api-Key") String key,// stored on string.xml
                                     @Query("sort_order") String sort,// already "best_match"
                                     @Query("phrase") String title);// title of the desired image


}
