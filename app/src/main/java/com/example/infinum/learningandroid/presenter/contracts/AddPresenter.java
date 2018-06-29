package com.example.infinum.learningandroid.presenter.contracts;

import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.model.interactors.contracts.StorageInteractor;

/**
 * Created by infinum on 04/08/2017.
 */

public interface AddPresenter {

    void onBackClicked();
    void onPokemonAdded(Pokemon pokemon);
    void pokemonInputChanged();
    void saveImage(byte[] imageData, StorageInteractor.StorageListener listener);

}
