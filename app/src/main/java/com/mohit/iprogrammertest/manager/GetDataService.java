package com.mohit.iprogrammertest.manager;

import com.mohit.iprogrammertest.model.ImageModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("photos")
    Call<List<ImageModel>> getPhotos();
}
