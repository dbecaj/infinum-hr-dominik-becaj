package com.example.infinum.learningandroid.model;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by infinum on 24/07/2017.
 */

@JsonApi(type = "users")
public class User extends Resource {

    @Json(name = "email") String email;
    @Json(name = "username") String username;
    @Json(name = "auth-token") String authToken;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getFormatedAuthorization() {
        return "Token token=" + authToken + ", email=" + email;
    }
}
