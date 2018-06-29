package com.example.infinum.learningandroid.model.interactors;

import android.support.annotation.Nullable;

import com.example.infinum.learningandroid.MyApplication;
import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.LoggedUser;
import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.model.PokemonJson;
import com.example.infinum.learningandroid.model.PokemonMove;
import com.example.infinum.learningandroid.model.PokemonType;
import com.example.infinum.learningandroid.model.api.PokemonApi;
import com.example.infinum.learningandroid.model.interactors.contracts.PokemonInteractor;
import com.example.infinum.learningandroid.network.RestService;
import com.example.infinum.learningandroid.utils.ResourceProvider;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import moe.banana.jsonapi2.ArrayDocument;
import moe.banana.jsonapi2.ObjectDocument;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by infinum on 02/08/2017.
 */

public class PokemonInteractorNetworkImpl implements PokemonInteractor {

    @Override
    public void getPokemons(final PokemonListener listener) {
        if(!LoggedUser.isUserLoggedIn()) {
            listener.onError(ResourceProvider.getString(R.string.error_user_not_logged_in));
            return;
        }

        PokemonApi pokemonApi = RestService.getRestApi().create(PokemonApi.class);
        Call<ArrayDocument<PokemonJson>> call = pokemonApi.getPokemons(
                LoggedUser.getLoggedUser().getFormatedAuthorization());
        call.enqueue(new Callback<ArrayDocument<PokemonJson>>() {
            @Override
            public void onResponse(Call<ArrayDocument<PokemonJson>> call,
                                   Response<ArrayDocument<PokemonJson>> response) {
                if (!response.isSuccessful()) {
                    listener.onError(ResourceProvider.getString(R.string.error_pokemon_list));
                    return;
                }

                List<Pokemon> pokemonList = new ArrayList<Pokemon>();
                for (PokemonJson pokemonJson : response.body()) {
                    Pokemon pokemon = new Pokemon();
                    // I think not setting the id was the problem but can't check since pokeapi is down
                    pokemon.setId(Integer.parseInt(pokemonJson.getId()));
                    pokemon.setName(pokemonJson.getName());
                    pokemon.setHeight(String.valueOf(pokemonJson.getHeigth()) + "m");
                    pokemon.setWeight(String.valueOf(pokemonJson.getWeight()) + "kg");
                    pokemon.setDescription(pokemonJson.getDescription());
                    String category = "N/A";
                    // Getting category from relationships does not work
                    /*for(PokemonType type : pokemonJson.getTypes()) {
                        if(type != null) {
                            category += types.get(type.getId()) + ",";
                        }
                        else {
                            Timber.d(String.valueOf(pokemonJson.getTypes().size()));
                        }
                    }*/
                    pokemon.setCategory(category);
                    String abilities = "N/A";
                    // Getting moves does not work either
                    /*
                    for(PokemonMove move : pokemonJson.getMoves()) {
                        if(move != null) {
                            abilities += moves.get(move.getId()) + ",";
                        }
                    }*/
                    pokemon.setAbilities(abilities);
                    pokemon.setGender(pokemonJson.getGender());
                    if (pokemonJson.hasImage()) {
                        pokemon.setImagePath(ResourceProvider.getString(R.string.api_url) +
                                pokemonJson.getImageUrl());
                    }

                    pokemonList.add(pokemon);
                }
                listener.onPokemonsReceived(pokemonList);
            }

            @Override
            public void onFailure(Call<ArrayDocument<PokemonJson>> call, Throwable t) {
                listener.onError(ResourceProvider.getString(R.string.error_cant_save));
                Timber.d(t.getMessage());
            }
        });
    }

    @Override
    public void savePokemon(Pokemon pokemon, @Nullable final PokemonSaveListener listener) {
        if(!LoggedUser.isUserLoggedIn()) {
            if(listener != null) {
                listener.onError(ResourceProvider.getString(R.string.error_user_not_logged_in));
            }
            return;
        }

        PokemonApi api = RestService.getRestApi().create(PokemonApi.class);
        PokemonJson json = Pokemon.toPokemonJson(pokemon);
        Call<ObjectDocument<PokemonJson>> call = api.createPokemon(
                LoggedUser.getLoggedUser().getFormatedAuthorization(), json);
        call.enqueue(new Callback<ObjectDocument<PokemonJson>>() {
            @Override
            public void onResponse(Call<ObjectDocument<PokemonJson>> call,
                                   Response<ObjectDocument<PokemonJson>> response) {
                if(!response.isSuccessful()) {
                    if(listener != null) {
                        listener.onError(ResourceProvider.getString(R.string.error_cant_save));
                    }
                    return;
                }

                if(listener != null) {
                    listener.onSuccess(Pokemon.fromPokemonJson(response.body().get()));
                }
            }

            @Override
            public void onFailure(Call<ObjectDocument<PokemonJson>> call, Throwable t) {
                if(listener != null) {
                    listener.onError(ResourceProvider.getString(R.string.error_cant_save));
                }
                Timber.d(t.getMessage());
            }
        });
    }

    public void syncPokemonDatabase(List<Pokemon> pokemonList) {
        SQLite.delete().from(Pokemon.class).execute();

        for(Pokemon pokemon : pokemonList) {
            pokemon.save();
        }
    }
}
