package com.example.infinum.learningandroid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.LoggedUser;
import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.presenter.MainPresenterImpl;
import com.example.infinum.learningandroid.presenter.contracts.MainPresenter;
import com.example.infinum.learningandroid.ui.contracts.MainView;
import com.example.infinum.learningandroid.ui.fragments.AddFragment;
import com.example.infinum.learningandroid.ui.fragments.DetailsFragment;
import com.example.infinum.learningandroid.ui.fragments.MainFragment;
import com.example.infinum.learningandroid.ui.list.PokemonListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AddFragment.AddListener,
        PokemonListAdapter.PokemonClickListener, MainView,
        NavigationView.OnNavigationItemSelectedListener {

    public static final int ADD_RESULT_ID = 1;
    private static final String ADD_FRAGMENT = "ADD_FRAGMENT";
    private static final String DETAILS_FRAGMENT = "DETAILS_FRAGMENT";
    private static final String FIRST_LOAD = "FIRST_LOAD";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.mainDrawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.mainNavigation)
    NavigationView navigationView;

    private MainPresenter presenter;
    private TextView navigationNameText;
    private TextView navigationEmailText;
    private boolean firstLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenterImpl(this);

        firstLoad = true;
        if(savedInstanceState != null) {
            firstLoad = savedInstanceState.getBoolean(FIRST_LOAD);
        }

        if(firstLoad) {
            presenter.loadPokemons();
        }
        else {
            presenter.loadDatabasePokemons();
        }

        initUi();
    }

    @Override
    public void initUi() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // toolbar.setTitle does nothing :/
        getSupportActionBar().setTitle(getString(R.string.pokemon_list_title));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter.initScreenConfiguration();

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_opened, R.string.drawer_closed);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);

        View navigationHeader = navigationView.getHeaderView(0);
        navigationNameText = navigationHeader.findViewById(R.id.navigationName);
        navigationEmailText = navigationHeader.findViewById(R.id.navigationEmail);

        if(!LoggedUser.isUserLoggedIn()) {
            showLoginScreen();
            return;
        }
        else {
            navigationNameText.setText(LoggedUser.getLoggedUser().getUsername());
            navigationEmailText.setText(LoggedUser.getLoggedUser().getEmail());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        firstLoad = false;
        outState.putBoolean(FIRST_LOAD, firstLoad);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MainFragment fragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mainFragment);
        // The event never fired in fragment even after the explicit super.onActivityResult()

        switch(resultCode) {
            case ADD_RESULT_ID:
                presenter.loadDatabasePokemons();
                break;
        }

        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mainAddPokemonButton:
                presenter.onAddPokemonAddClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.drawerLogoutButton) {
            presenter.onLogoutClicked();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        presenter.onBackClicked();
    }

    public MainPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onPokemonAdded(Pokemon pokemon) {
        presenter.onPokemonAdded(pokemon);
    }

    @Override
    public void onPokemonClicked(Pokemon pokemon) {
        presenter.onPokemonClicked(pokemon);
    }

    @Override
    public void showProgress() {
        MainFragment fragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mainFragment);
        fragment.showProgress();
    }

    @Override
    public void hideProgress() {
        MainFragment fragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mainFragment);
        fragment.hideProgress();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPokemons(List<Pokemon> pokemonList) {
        MainFragment fragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mainFragment);

        if(pokemonList.isEmpty()) {
            fragment.showNoPokemons();
            return;
        }

        fragment.addPokemonsToList(pokemonList);
    }

    @Override
    public void showDetailsScreen(Pokemon pokemon) {
        Intent intent = DetailsActivity.buildIntent(this, pokemon);
        startActivity(intent);
    }

    @Override
    public void addNewPokmeon(Pokemon pokemon) {
        MainFragment fragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mainFragment);
        fragment.addPokemonToList(pokemon);
    }

    @Override
    public void showAddPokemonScreen() {
        Intent addActivity = new Intent(this, AddActivity.class);
        startActivityForResult(addActivity, ADD_RESULT_ID);
    }

    @Override
    public void showAddFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AddFragment fragment = new AddFragment();
        transaction.replace(R.id.mainSecondFragmentPlaceholder, fragment, ADD_FRAGMENT);
        transaction.commit();

        fragment.addAddListener(this);
    }

    @Override
    public void showDetailsFragment(Pokemon pokemon) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainSecondFragmentPlaceholder, DetailsFragment.newInstance(pokemon),
                DETAILS_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
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
    public void quitApplication() {
        super.onBackPressed();
    }

    @Override
    public void onInputChanged() {
    }

    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }
}
