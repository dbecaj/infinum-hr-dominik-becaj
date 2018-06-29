package com.example.infinum.learningandroid.model;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by infinum on 24/07/2017.
 */

@JsonApi(type = "session")
public class LoginUser extends Resource {

    @Json(name = "email") String email;
    @Json(name = "password") String password;

    public LoginUser() {
        email = "";
        password = "";
    }

    public LoginUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
