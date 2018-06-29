package com.example.infinum.learningandroid.model;

import android.support.annotation.Nullable;

import com.squareup.moshi.Json;

import java.util.List;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by infinum on 26/07/2017.
 */

@JsonApi(type = "pokemons")
public class PokemonJson extends Resource {

    @Json(name = "name") String name;
    @Json(name = "height") double heigth;
    @Json(name = "weight") double weight;
    @Json(name = "image-url") String imageUrl;
    @Json(name = "description") String description;
    @Json(name = "gender") String gender;
    @Json(name = "gender-id") String genderId;
    @Json(name = "types") HasMany<PokemonType> types;
    @Json(name = "moves") HasMany<PokemonMove> moves;

    protected PokemonJson() {

    }

    public String getName() {
        return name;
    }

    public double getHeigth() {
        return heigth;
    }

    public double getWeight() {
        return weight;
    }

    public boolean hasImage() {
        return imageUrl != null;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return gender;
    }

    public String getGenderId() {
        return genderId;
    }

    public List<PokemonType> getTypes() {
        return types.get(getContext());
    }

    public List<PokemonMove> getMoves() {
        return moves.get(getContext());
    }

    public static class Builder {
        String name;
        double height;
        double weight;
        String imageUrl;
        String description;
        String genderId;
        HasMany<PokemonType> types;
        HasMany<PokemonMove> moves;

        public Builder(String name, double height, double weight, String description, String genderId) {
            this.name = name;
            this.height = height;
            this.weight = weight;
            this.description = description;
            this.genderId = genderId;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;

            return this;
        }

        public PokemonJson build() {
            return new PokemonJson(name, height, weight, imageUrl, description, genderId, types, moves);
        }
    }

    private PokemonJson(String name, double heigth, double weight, String imageUrl, String description,
                       String genderId, HasMany<PokemonType> types, HasMany<PokemonMove> moves) {
        this.name = name;
        this.heigth = heigth;
        this.weight = weight;
        this.imageUrl = imageUrl;
        this.description = description;
        this.genderId = genderId == "M" ? "1" : "2";
        this.types = types;
        this.moves = moves;
    }
}
