package com.example.infinum.learningandroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.SignUpUser;
import com.example.infinum.learningandroid.presenter.SignUpPresenterImpl;
import com.example.infinum.learningandroid.presenter.contracts.SignUpPresenter;
import com.example.infinum.learningandroid.ui.contracts.SignUpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by infinum on 20/07/2017.
 */

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.signUpEmailInput)
    EditText emailInput;

    @BindView(R.id.signUpNicknameInput)
    EditText nicknameInput;

    @BindView(R.id.signUpPasswordInput)
    EditText passwordInput;

    @BindView(R.id.signUpConfirmUpPasswordInput)
    EditText confirmPasswordInput;

    @BindView(R.id.signUpProgressBar)
    ProgressBar progressBar;

    private SignUpPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        presenter = new SignUpPresenterImpl(this);

        initUi();
    }

    @Override
    public void initUi() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.sign_up));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.handleUnfinishedNetworkCalls();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();

        return super.onSupportNavigateUp();
    }

    @OnClick(R.id.signUpSignUpButton)
    protected void onSignUpClicked() {
        String email = emailInput.getText().toString();
        String username = nicknameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        SignUpUser user = new SignUpUser(email, username, password, confirmPassword);

        presenter.signUpUser(user);
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);

        emailInput.setFocusable(false);
        nicknameInput.setFocusable(false);
        passwordInput.setFocusable(false);
        confirmPasswordInput.setFocusable(false);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);

        emailInput.setFocusableInTouchMode(true);
        nicknameInput.setFocusableInTouchMode(true);
        passwordInput.setFocusableInTouchMode(true);
        confirmPasswordInput.setFocusableInTouchMode(true);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCredentialError(SignUpCredentialType type, String error) {
        switch(type) {
            case EMAIL:
                emailInput.setError(error);
                break;
            case NICKNAME:
                nicknameInput.setError(error);
                break;
            case PASSWORD:
                passwordInput.setError(error);
                break;
            case CONFIRM_PASSWORD:
                confirmPasswordInput.setError(error);
                break;
            default:
                Timber.d("No such SignUpCredentialType!");
        }
    }

    @Override
    public void showMainScreen() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }
}
