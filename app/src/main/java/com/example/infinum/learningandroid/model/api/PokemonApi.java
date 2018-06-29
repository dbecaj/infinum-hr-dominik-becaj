package com.example.infinum.learningandroid.model.api;

import com.example.infinum.learningandroid.model.PokemonJson;
import com.example.infinum.learningandroid.model.PokemonMove;
import com.example.infinum.learningandroid.model.PokemonType;

import moe.banana.jsonapi2.ArrayDocument;
import moe.banana.jsonapi2.ObjectDocument;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by infinum on 26/07/2017.
 */

public interface PokemonApi {

    @GET("/api/v1/pokemons")
    Call<ArrayDocument<PokemonJson>> getPokemons(@Header("Authorization") String tokenAndEmail);

    @GET("/api/v1/types")
    Call<ArrayDocument<PokemonType>> getTypes(@Header("Authorization") String tokenAndEmail);

    @GET("/api/v1/moves")
    Call<ArrayDocument<PokemonMove>> getMoves(@Header("Authorization") String tokenAndEmail);

    @POST("/api/v1/pokemons")
    Call<ObjectDocument<PokemonJson>> createPokemon(@Header("Authorization") String tokenAndEmail,
                                                    @Body PokemonJson pokemon);

}
