package com.example.infinum.learningandroid.model;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by infinum on 24/07/2017.
 */

@JsonApi(type = "users")
public class SignUpUser extends Resource {

    @Json(name = "email") String email;
    @Json(name = "username") String username;
    @Json(name = "password") String password;
    @Json(name = "password_confirmation") String confirmPassword;

    public SignUpUser() {
    }

    public SignUpUser(String email, String username, String password, String confirmPassword) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
