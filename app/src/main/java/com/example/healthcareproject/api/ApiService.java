package com.example.healthcareproject.api;
import com.example.healthcareproject.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {


    @FormUrlEncoded
    @POST("api/register")
    Call<String> register(
            @Field("username") String username,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("api/login")
    Call<String> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("api/users")
    Call<List<User>> getUsers();
}