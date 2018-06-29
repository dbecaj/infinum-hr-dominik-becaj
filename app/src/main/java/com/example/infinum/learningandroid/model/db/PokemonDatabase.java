package com.example.infinum.learningandroid.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by infinum on 14/07/2017.
 */

@Database(name = PokemonDatabase.NAME,version = PokemonDatabase.VERSION)
public class PokemonDatabase {

    public static final String NAME = "PokemonDatabase";
    public static final int VERSION = 1;

}
