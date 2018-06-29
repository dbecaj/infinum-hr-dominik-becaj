package com.example.infinum.learningandroid.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.presenter.contracts.MainPresenter;
import com.example.infinum.learningandroid.ui.MainActivity;
import com.example.infinum.learningandroid.ui.list.PokemonListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by infinum on 18/07/2017.
 */

public class MainFragment extends Fragment {

    @BindView(R.id.mainRecyclerList)
    RecyclerView pokemonList;

    @BindView(R.id.mainNoPokemonsLayout)
    LinearLayout noPokemons;

    @BindView(R.id.mainPokemonListProgressBar)
    ProgressBar loadProgressBar;

    @BindView(R.id.mainSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private PokemonListAdapter pokemonAdapter;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        pokemonList.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Pokemon> pokemons = new ArrayList<>();
        pokemonAdapter = new PokemonListAdapter(pokemons);
        pokemonAdapter.setClickListener((MainActivity)getActivity());
        pokemonList.setAdapter(pokemonAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).getPresenter().loadPokemons();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void addPokemonToList(Pokemon pokemon) {
        pokemonAdapter.addPokemon(pokemon);
        if(noPokemons.getVisibility() == View.VISIBLE) {
            hideNoPokemons();
        }
    }

    public void addPokemonsToList(List<Pokemon> pokemons) {
        pokemonAdapter.clearList();

        if(noPokemons.getVisibility() == View.VISIBLE) {
            hideNoPokemons();
        }

        for(Pokemon pokemon : pokemons) {
            pokemonAdapter.addPokemon(pokemon);
        }
    }

    public void showProgress() {
        if(!swipeRefreshLayout.isRefreshing()) {
            loadProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        loadProgressBar.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    public void showNoPokemons() {
        noPokemons.setVisibility(View.VISIBLE);
    }

    public void hideNoPokemons() {
        noPokemons.setVisibility(View.INVISIBLE);
    }
}
