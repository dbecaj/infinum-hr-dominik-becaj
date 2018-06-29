package com.example.infinum.learningandroid.model.interactors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.model.Pokemon_Table;
import com.example.infinum.learningandroid.model.db.PokemonDatabase;
import com.example.infinum.learningandroid.model.interactors.contracts.PokemonInteractor;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.List;

import timber.log.Timber;

/**
 * Created by infinum on 02/08/2017.
 */

public class PokemonInteractorDatabaseImpl implements PokemonInteractor {

    @Override
    public void getPokemons(PokemonListener listener) {
        List<Pokemon> pokemonList = SQLite.select().from(Pokemon.class).orderBy(Pokemon_Table.id, false)
                .queryList();

        listener.onPokemonsReceived(pokemonList);
    }

    @Override
    public void savePokemon(final Pokemon pokemon, @Nullable final PokemonSaveListener listener) {
        DatabaseDefinition database = FlowManager.getDatabase(PokemonDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                pokemon.save();
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                if(listener != null) {
                    listener.onSuccess(pokemon);
                }
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                if(listener != null) {
                    listener.onError(error.getMessage());
                }
            }
        }).build();

        transaction.execute();
    }

    public void updatePokemonImagePath(int id, String imagePath) {
        SQLite.update(Pokemon.class).set(Pokemon_Table.imagePath.eq(imagePath))
                .where(Pokemon_Table.id.eq(id)).execute();
    }
}
