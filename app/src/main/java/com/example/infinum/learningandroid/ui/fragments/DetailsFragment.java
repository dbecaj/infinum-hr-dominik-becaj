package com.example.infinum.learningandroid.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.ui.DetailsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by infinum on 19/07/2017.
 */

public class DetailsFragment extends Fragment {

    private static final String POKEMON = "POKEMON";

    @BindView(R.id.detailsName)
    TextView pokemonName;

    @BindView(R.id.detailsHeight)
    TextView pokemonHeight;

    @BindView(R.id.detailsWeight)
    TextView pokemonWeight;

    @BindView(R.id.detailsCategory)
    TextView pokemonCategory;

    @BindView(R.id.detailsAbilities)
    TextView pokemonAbilities;

    @BindView(R.id.detailsDescription)
    TextView pokemonDescription;

    @BindView(R.id.detailsImage)
    ImageView pokemonImage;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;

    public static DetailsFragment newInstance(Pokemon pokemon) {
        Bundle args = new Bundle();
        args.putParcelable(POKEMON, pokemon);

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        Pokemon pokemon = getArguments().getParcelable(POKEMON);
        updateData(pokemon);

        ((DetailsActivity)getActivity()).setupToolbar(toolbar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void updateData(Pokemon pokemon) {
        pokemonName.setText(pokemon.getName());
        pokemonHeight.setText(pokemon.getHeight());
        pokemonWeight.setText(pokemon.getWeight());
        pokemonCategory.setText(pokemon.getCategory());
        pokemonAbilities.setText(pokemon.getAbilities());
        pokemonDescription.setText(pokemon.getDescription());
        if(pokemon.hasImage()) {
            pokemonImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getActivity()).load(pokemon.getImagePath()).into(pokemonImage);
        }
    }
}
