package com.example.infinum.learningandroid.presenter;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.model.interactors.PokemonInteractorDatabaseImpl;
import com.example.infinum.learningandroid.model.interactors.PokemonInteractorNetworkImpl;
import com.example.infinum.learningandroid.model.interactors.StorageInteractorImpl;
import com.example.infinum.learningandroid.model.interactors.contracts.PokemonInteractor;
import com.example.infinum.learningandroid.model.interactors.contracts.StorageInteractor;
import com.example.infinum.learningandroid.presenter.contracts.AddPresenter;
import com.example.infinum.learningandroid.ui.contracts.AddView;
import com.example.infinum.learningandroid.utils.ResourceProvider;
import com.example.infinum.learningandroid.utils.SystemStateProvider;

import timber.log.Timber;

/**
 * Created by infinum on 04/08/2017.
 */

public class AddPresenterImpl implements AddPresenter {

    private AddView view;
    private boolean isSaved;

    public AddPresenterImpl(AddView view) {
        this.view = view;
        isSaved = false;
    }

    @Override
    public void onBackClicked() {
        if(isSaved) {
            view.finishAdd();
        }
        else {
            view.showSaveDialog();
        }
    }

    @Override
    public void onPokemonAdded(final Pokemon pokemon) {
        if(!SystemStateProvider.hasInternetConnection()) {
            view.showMessage(ResourceProvider.getString(R.string.error_no_internet));
        }

        PokemonInteractorNetworkImpl interactor = new PokemonInteractorNetworkImpl();
        view.showProgress();
        interactor.savePokemon(pokemon, new PokemonInteractor.PokemonSaveListener() {
            @Override
            public void onSuccess(Pokemon pokemon) {
                view.hideProgress();
                view.showMessage(ResourceProvider.getString(R.string.saved));
                PokemonInteractorDatabaseImpl databaseInteractor = new PokemonInteractorDatabaseImpl();
                databaseInteractor.savePokemon(pokemon, new PokemonInteractor.PokemonSaveListener() {
                    @Override
                    public void onSuccess(Pokemon pokemon) {
                        view.finishAdd();
                    }

                    @Override
                    public void onError(String error) {
                        view.showMessage(ResourceProvider.getString(R.string.error_database_save));
                        Timber.d(error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                view.hideProgress();
                view.showMessage(error);
            }
        });
    }

    @Override
    public void pokemonInputChanged() {
        isSaved = false;
    }

    @Override
    public void saveImage(byte[] imageData, StorageInteractor.StorageListener  listener) {
        StorageInteractorImpl interactor = new StorageInteractorImpl();
        interactor.saveImage(imageData, listener);
    }
}
