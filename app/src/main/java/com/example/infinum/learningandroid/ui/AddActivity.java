package com.example.infinum.learningandroid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.presenter.AddPresenterImpl;
import com.example.infinum.learningandroid.presenter.contracts.AddPresenter;
import com.example.infinum.learningandroid.ui.contracts.AddView;
import com.example.infinum.learningandroid.ui.fragments.AddFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by infinum on 11/07/2017.
 */

public class AddActivity extends AppCompatActivity implements AddView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AddPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        presenter = new AddPresenterImpl(this);

        initUi();
    }

    @Override
    public void initUi() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.add_new_title));

        AddFragment fragment = (AddFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addFragmentContainer);
        fragment.addAddListener(new AddFragment.AddListener() {
            @Override
            public void onPokemonAdded(Pokemon pokemon) {
                presenter.onPokemonAdded(pokemon);
            }

            @Override
            public void onInputChanged() {
                presenter.pokemonInputChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getMenuInflater().inflate(R.menu.add_menu_land, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        presenter.onBackClicked();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AddFragment fragment = (AddFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addFragmentContainer);
        // The event never fired in fragment even after the explicit super.onActivityResult()
        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AddFragment fragment = (AddFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addFragmentContainer);
        // The event never fired in fragment even after the explicit super.onRequestPermissionsResult()
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.addSaveButton_menu) {
            AddFragment fragment = (AddFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.addFragmentContainer);
            fragment.savePokemon();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        AddFragment fragment = (AddFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addFragmentContainer);
        fragment.showProgress();
    }

    @Override
    public void hideProgress() {
        AddFragment fragment = (AddFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addFragmentContainer);
        fragment.hideProgress();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSaveDialog() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch(which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        AddFragment fragment = (AddFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.addFragmentContainer);
                        fragment.savePokemon();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        finish();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.not_saved)).setNegativeButton(getString(R.string.no),
                listener).setPositiveButton(getString(R.string.yes), listener).show();
    }

    @Override
    public void finishAdd() {
        setResult(MainActivity.ADD_RESULT_ID);
        finish();
    }
}
