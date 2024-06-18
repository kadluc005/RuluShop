package com.example.rulushop.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PayGateGlobalAPI {
    @Headers("Content-Type: application/json")
    @POST("api/v1/pay")
    Call<JsonObject> requestPayment(@Body JsonObject body);
}
