package com.example.infinum.learningandroid.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.LoggedUser;
import com.example.infinum.learningandroid.model.LoginUser;
import com.example.infinum.learningandroid.presenter.LoginPresenterImpl;
import com.example.infinum.learningandroid.presenter.contracts.LoginPresenter;
import com.example.infinum.learningandroid.ui.contracts.LoginView;
import com.example.infinum.learningandroid.ui.views.LogoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by infinum on 20/07/2017.
 */

public class LoginActivity extends AppCompatActivity implements LoginView {

    public final static String IS_FIRST = "IS_FIRST";

    @BindView(R.id.loginEmailInput)
    EditText emailInput;

    @BindView(R.id.loginPasswordInput)
    EditText passwordInput;

    @BindView(R.id.loginPasswordVisibilityButton)
    ImageButton passwordVisibilityButton;

    @BindView(R.id.loginProgressBar)
    ProgressBar progressBar;

    @BindView(R.id.loginLogo)
    LogoView logoView;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LoginPresenterImpl(this);

        initUi();

        if(savedInstanceState != null) {
            if(savedInstanceState.getBoolean(IS_FIRST)) {
                logoView.playAnimation();
            }
        }
        else {
            logoView.playAnimation();
        }

        presenter.automaticLogin();
    }

    @Override
    public void initUi() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        if(LoggedUser.isUserLoggedIn()) {
            startMainActivity();
        }

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean(IS_FIRST, true);

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.handleUnfinishedNetworkCalls();
    }

    @OnClick(R.id.loginLoginButton)
    protected void onLoginClicked() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        LoginUser user = new LoginUser(email, password);

        presenter.onLoginClicked(user);
    }

    @OnClick(R.id.loginPasswordVisibilityButton)
    protected void onPasswordVisibilityClicked() {
        if(passwordInput.getInputType() == (InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            showPassword();
        }
        else {
            hidePassword();
        }
    }

    @OnClick(R.id.loginSignUpButton)
    protected void onSignUpClicked() {
        // I know the call somewhat redundant but it is more future proof if we want to expand
        presenter.onSignUpClicked();
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);

        emailInput.setFocusable(false);
        passwordInput.setFocusable(false);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);

        emailInput.setFocusableInTouchMode(true);
        passwordInput.setFocusableInTouchMode(true);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCredentialsError(LoginCredentialType type, String error) {
        switch(type) {
            case EMAIL:
                emailInput.setError(error);
                break;
            case PASSWORD:
                passwordInput.setError(error);
                break;
            default:
                Timber.d("No such LoginCredentialType");
                break;
        }
    }

    @Override
    public void showPassword() {
        passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        passwordVisibilityButton.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_visibility_on));
    }

    @Override
    public void hidePassword() {
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordVisibilityButton.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_visibility_off));
    }

    @Override
    public void showMainScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }

    @Override
    public void showSignUpScreen() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void fillEmailAndPassword(String email, String password) {
        emailInput.setText(email);
        passwordInput.setText(password);
    }
}
