package com.example.infinum.learningandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.infinum.learningandroid.model.db.PokemonDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by infinum on 12/07/2017.
 */

@Table(database = PokemonDatabase.class)
public class Pokemon extends BaseModel implements Parcelable {

    @Column
    @PrimaryKey
    private int id;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String description;

    @Column
    @NotNull
    private String weight;

    @Column
    @NotNull
    private String height;

    @Column
    @NotNull
    private String category;

    @Column
    @NotNull
    private String abilities;

    @Column
    @NotNull
    private String gender;

    @Column
    private String imagePath;

    public Pokemon() {

    }

    public Pokemon(Parcel source) {
        id = source.readInt();
        name = source.readString();
        height = source.readString();
        weight = source.readString();
        category = source.readString();
        abilities = source.readString();
        description = source.readString();
        gender = source.readString();
        imagePath = source.readString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAbilities(String abilities) {
        this.abilities = abilities;
    }

    public String getGender() {
        return gender;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasImage() {
        return imagePath != null && !imagePath.isEmpty();
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getCategory() {
        return category;
    }

    public String getAbilities() {
        return abilities;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(height);
        parcel.writeString(weight);
        parcel.writeString(category);
        parcel.writeString(abilities);
        parcel.writeString(description);
        parcel.writeString(gender);
        parcel.writeString(imagePath);
    }

    public static final Creator<Pokemon> CREATOR = new Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel parcel) {
            return new Pokemon(parcel);
        }

        @Override
        public Pokemon[] newArray(int i) {
            return new Pokemon[i];
        }
    };

    public static PokemonJson toPokemonJson(Pokemon pokemon) {
        PokemonJson.Builder builder = new PokemonJson.Builder(pokemon.getName(),
                Double.parseDouble(pokemon.getHeight()),
                Double.parseDouble(pokemon.getWeight()), pokemon.getDescription(), pokemon.getGender());
        if(pokemon.hasImage()) {
            builder.imageUrl(pokemon.getImagePath());
        }

        return builder.build();
    }

    public static Pokemon fromPokemonJson(PokemonJson pokemonJson) {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(Integer.parseInt(pokemonJson.getId()));
        pokemon.setName(pokemonJson.getName());
        pokemon.setHeight(String.valueOf(pokemonJson.getHeigth()));
        pokemon.setWeight(String.valueOf(pokemonJson.getWeight()));
        pokemon.setCategory("N/A");
        pokemon.setAbilities("N/A");
        pokemon.setGender(pokemonJson.getGender());
        pokemon.setDescription(pokemonJson.getDescription());

        return pokemon;
    }
}
