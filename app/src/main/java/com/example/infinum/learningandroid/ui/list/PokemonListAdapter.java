package com.example.infinum.learningandroid.ui.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.model.interactors.PokemonInteractorDatabaseImpl;
import com.example.infinum.learningandroid.model.interactors.StorageInteractorImpl;
import com.example.infinum.learningandroid.model.interactors.contracts.StorageInteractor;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by infinum on 12/07/2017.
 */

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder> {

    private List<Pokemon> pokemons;
    private List<PokemonClickListener> clickListeners;

    public interface PokemonClickListener {
        void onPokemonClicked(Pokemon pokemon);
    }

    public PokemonListAdapter(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
        clickListeners = new ArrayList<>();
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemName)
        public TextView pokemonName;

        @BindView(R.id.itemImage)
        public ImageView pokemonImage;

        public PokemonViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).
                inflate(R.layout.pokemon_list_item, parent, false);

        return new PokemonViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, final int position) {
        final Pokemon pokemon = pokemons.get(position);
        holder.pokemonName.setText(pokemon.getName());
        if(pokemon.hasImage()) {
            Glide.with(holder.pokemonImage.getContext()).load(pokemon.getImagePath())
                    .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model,
                                               Target<Drawable> target, DataSource dataSource,
                                               boolean isFirstResource) {
                    Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] imageData = stream.toByteArray();

                    StorageInteractorImpl interactor = new StorageInteractorImpl();
                    interactor.saveImage(imageData, new StorageInteractor.StorageListener() {
                        @Override
                        public void onSuccess(String imagePath) {
                            PokemonInteractorDatabaseImpl databaseInteractor =
                                    new PokemonInteractorDatabaseImpl();
                            databaseInteractor.updatePokemonImagePath(pokemon.getId(), imagePath);
                        }

                        @Override
                        public void onError(String error) {
                            Timber.d("Could not save the image to local storage: " + error);
                        }
                    });
                    return false;
                }
            }).into(holder.pokemonImage);
        }
        else {
            Glide.with(holder.pokemonImage.getContext()).load(R.drawable.ic_image_black_48dp)
                    .into(holder.pokemonImage);
        }

        for(final PokemonClickListener listener : clickListeners) {
            final int index = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPokemonClicked(pokemons.get(index));
                }
            });
        }
    }

    public void setClickListener(PokemonClickListener listener) {
        clickListeners.add(listener);
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public void addPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
        notifyItemInserted(pokemons.size() - 1);
    }

    public void clearList() {
        int size = pokemons.size();
        pokemons.clear();
        notifyItemRangeRemoved(0, size);
    }
}
