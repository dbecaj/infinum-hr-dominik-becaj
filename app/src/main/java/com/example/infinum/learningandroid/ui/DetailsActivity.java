package com.example.infinum.learningandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.ui.contracts.DetailsView;
import com.example.infinum.learningandroid.ui.fragments.DetailsFragment;

import butterknife.ButterKnife;

/**
 * Created by infinum on 11/07/2017.
 */

public class DetailsActivity extends AppCompatActivity implements DetailsView {

    private static final String POKEMON = "POKEMON";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initUi();
    }

    @Override
    public void initUi() {
        ButterKnife.bind(this);

        Pokemon pokemon = getIntent().getParcelableExtra(POKEMON);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.detailsFragmentPlaceholder,
                DetailsFragment.newInstance(pokemon));
        transaction.commit();
    }

    public static Intent buildIntent(Context context, Pokemon pokemon) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(POKEMON, pokemon);

        return intent;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateViewData(Pokemon pokemon) {
        DetailsFragment fragment = (DetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailsFragmentPlaceholder);
        fragment.updateData(pokemon);
    }

    public void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
