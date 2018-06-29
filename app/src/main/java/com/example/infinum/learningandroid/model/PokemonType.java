package com.example.infinum.learningandroid.model;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by infinum on 27/07/2017.
 */

@JsonApi(type = "types")
public class PokemonType extends Resource {

    @Json(name = "name") String name;

    public String getName() {
        return name;
    }

}
