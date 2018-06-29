package com.example.infinum.learningandroid.presenter;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.LoggedUser;
import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.model.interactors.PokemonInteractorDatabaseImpl;
import com.example.infinum.learningandroid.model.interactors.PokemonInteractorNetworkImpl;
import com.example.infinum.learningandroid.model.interactors.contracts.PokemonInteractor;
import com.example.infinum.learningandroid.presenter.contracts.MainPresenter;
import com.example.infinum.learningandroid.ui.contracts.MainView;
import com.example.infinum.learningandroid.utils.ResourceProvider;
import com.example.infinum.learningandroid.utils.SystemStateProvider;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by infinum on 02/08/2017.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainView view;
    private boolean dualPane;

    public MainPresenterImpl(MainView view) {
        this.view = view;
        dualPane = SystemStateProvider.doesSupportDualPane();
    }

    @Override
    public void initScreenConfiguration() {
        if(dualPane) {
            view.showAddFragment();
        }
    }

    @Override
    public void loadPokemons() {
        view.showProgress();
        if(SystemStateProvider.hasInternetConnection()) {
            loadNetworkPokemons();
        }
        else {
            loadDatabasePokemons();
        }
    }

    @Override
    public void loadNetworkPokemons() {
        final PokemonInteractorNetworkImpl interactor = new PokemonInteractorNetworkImpl();
        interactor.getPokemons(new PokemonInteractor.PokemonListener() {
            @Override
            public void onPokemonsReceived(List<Pokemon> pokemonList) {
                view.showPokemons(pokemonList);
                interactor.syncPokemonDatabase(pokemonList);
                view.hideProgress();
            }

            @Override
            public void onError(String error) {
                view.showMessage(error);
                view.hideProgress();
                loadDatabasePokemons();
            }
        });
    }

    @Override
    public void loadDatabasePokemons() {
        PokemonInteractor interactor = new PokemonInteractorDatabaseImpl();
        interactor.getPokemons(new PokemonInteractor.PokemonListener() {
            @Override
            public void onPokemonsReceived(List<Pokemon> pokemonList) {
                view.showPokemons(pokemonList);
                view.hideProgress();
            }

            @Override
            public void onError(String error) {
                view.showMessage(error);
                view.hideProgress();
            }
        });
    }

    @Override
    public void onPokemonClicked(Pokemon pokemon) {
        if(dualPane) {
            view.showDetailsFragment(pokemon);
        }
        else {
            view.showDetailsScreen(pokemon);
        }
    }

    @Override
    public void onAddPokemonAddClicked() {
        if(dualPane) {
            view.showAddFragment();
        }
        else {
            view.showAddPokemonScreen();
        }
    }

    @Override
    public void onBackClicked() {
        if(dualPane) {
            view.showSaveDialog();
        }
        else {
            view.quitApplication();
        }
    }

    @Override
    public void onPokemonAdded(Pokemon pokemon) {
        if(!SystemStateProvider.hasInternetConnection()) {
            view.showMessage(ResourceProvider.getString(R.string.error_no_internet));
            return;
        }

        PokemonInteractor interactor = new PokemonInteractorNetworkImpl();
        interactor.savePokemon(pokemon, new PokemonInteractor.PokemonSaveListener() {
            @Override
            public void onSuccess(Pokemon pokemon) {
                view.addNewPokmeon(pokemon);
            }

            @Override
            public void onError(String error) {
                view.showMessage(error);
            }
        });
    }

    @Override
    public void onLogoutClicked() {
        LoggedUser.logoutUser();
        view.showLoginScreen();
    }

}
