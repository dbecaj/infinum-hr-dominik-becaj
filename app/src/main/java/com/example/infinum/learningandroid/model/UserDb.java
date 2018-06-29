package com.example.infinum.learningandroid.model;

import com.example.infinum.learningandroid.model.db.PokemonDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by infinum on 09/08/2017.
 */

@Table(database = PokemonDatabase.class)
public class UserDb extends BaseModel {

    @PrimaryKey
    @Column
    String email;

    @Column
    @NotNull
    String password;

    public UserDb() {

    }

    public UserDb(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
