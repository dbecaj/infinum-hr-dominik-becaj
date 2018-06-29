package com.example.infinum.learningandroid.presenter.contracts;

import com.example.infinum.learningandroid.model.Pokemon;

/**
 * Created by infinum on 02/08/2017.
 */

public interface MainPresenter {

    void initScreenConfiguration();
    void loadPokemons();
    void loadNetworkPokemons();
    void loadDatabasePokemons();
    void onPokemonClicked(Pokemon pokemon);
    void onAddPokemonAddClicked();
    void onBackClicked();
    void onPokemonAdded(Pokemon pokemon);
    void onLogoutClicked();

}
