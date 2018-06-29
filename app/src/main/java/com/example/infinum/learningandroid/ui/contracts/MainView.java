package com.example.infinum.learningandroid.ui.contracts;

import com.example.infinum.learningandroid.model.Pokemon;

import java.util.List;

/**
 * Created by infinum on 03/08/2017.
 */

public interface MainView extends BaseView {

    enum FragmentType {
        ADD_FRAGMENT,
        DETAILS_FRAGMENT
    }

    void showPokemons(List<Pokemon> pokemonList);
    void showDetailsScreen(Pokemon pokemon);
    void addNewPokmeon(Pokemon pokemon);
    void showAddPokemonScreen();
    void showAddFragment();
    void showDetailsFragment(Pokemon pokemon);
    void showSaveDialog();
    void quitApplication();
    void showLoginScreen();

}
