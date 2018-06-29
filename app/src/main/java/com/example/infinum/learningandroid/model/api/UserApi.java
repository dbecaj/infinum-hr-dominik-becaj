package com.example.infinum.learningandroid.model.api;

import com.example.infinum.learningandroid.model.LoginUser;
import com.example.infinum.learningandroid.model.SignUpUser;
import com.example.infinum.learningandroid.model.User;

import moe.banana.jsonapi2.ObjectDocument;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by infinum on 23/07/2017.
 */

public interface UserApi {

    @POST("/api/v1/users/login")
    Call<ObjectDocument<User>> loginUser(@Body ObjectDocument<LoginUser> user);

    @POST("/api/v1/users")
    Call<ObjectDocument<User>> signUpUser(@Body ObjectDocument<SignUpUser> user);

    @GET("/api/v1/users/{id}")
    Call<ObjectDocument<User>> getUser(@Path("id") String id);

}
