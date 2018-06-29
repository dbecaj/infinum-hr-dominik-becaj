package com.example.infinum.learningandroid.model.interactors.contracts;

import android.support.annotation.Nullable;

import com.example.infinum.learningandroid.model.Pokemon;

import java.util.List;

/**
 * Created by infinum on 02/08/2017.
 */

public interface PokemonInteractor {

    interface PokemonListener {
        void onPokemonsReceived(List<Pokemon> pokemonList);
        void onError(String error);
    }

    interface PokemonSaveListener {
        void onSuccess(Pokemon pokemon);
        void onError(String error);
    }

    void getPokemons(PokemonListener listener);
    void savePokemon(Pokemon pokemon, @Nullable PokemonSaveListener listener);

}
