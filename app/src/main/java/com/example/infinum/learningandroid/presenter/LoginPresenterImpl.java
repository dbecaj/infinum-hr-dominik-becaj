package com.example.infinum.learningandroid.presenter;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.LoggedUser;
import com.example.infinum.learningandroid.model.LoginUser;
import com.example.infinum.learningandroid.model.User;
import com.example.infinum.learningandroid.model.UserDb;
import com.example.infinum.learningandroid.model.interactors.UserInteractorNetworkImpl;
import com.example.infinum.learningandroid.model.interactors.contracts.UserInteractor;
import com.example.infinum.learningandroid.presenter.contracts.LoginPresenter;
import com.example.infinum.learningandroid.ui.contracts.LoginView;
import com.example.infinum.learningandroid.utils.AppUtils;
import com.example.infinum.learningandroid.utils.ResourceProvider;
import com.example.infinum.learningandroid.utils.SystemStateProvider;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import moe.banana.jsonapi2.ObjectDocument;
import retrofit2.Call;

/**
 * Created by infinum on 02/08/2017.
 */

public class LoginPresenterImpl implements LoginPresenter {

    private Call<ObjectDocument<User>> loginCall;
    private LoginView view;
    private UserInteractor interactor;

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
        interactor = new UserInteractorNetworkImpl();
    }

    @Override
    public void handleUnfinishedNetworkCalls() {
        if(loginCall != null) {
            loginCall.cancel();
        }
    }

    @Override
    public void onLoginClicked(final LoginUser user) {
        if(!SystemStateProvider.hasInternetConnection()) {
            view.showMessage(ResourceProvider.getString(R.string.error_no_internet));
            return;
        }

        if(!checkInputs(user.getEmail(), user.getPassword())) {
            return;
        }

        view.showProgress();
        loginCall = interactor.loginUser(user, new UserInteractor.UserListener() {
            @Override
            public void onSuccess(User u) {
                view.hideProgress();
                LoggedUser.cacheUser(user);
                LoggedUser.loginUser(u);
                view.showMainScreen();
            }

            @Override
            public void onError(String error) {
                view.hideProgress();
                view.showMessage(error);
            }
        });
    }

    @Override
    public boolean checkInputs(String email, String password) {
        if(email.isEmpty()) {
            view.showCredentialsError(LoginView.LoginCredentialType.EMAIL,
                    ResourceProvider.getString(R.string.error_email_empty));
            return false;
        }
        else if(!AppUtils.isEmailValid(email)) {
            view.showCredentialsError(LoginView.LoginCredentialType.EMAIL,
                    ResourceProvider.getString(R.string.error_not_email));
            return false;
        }
        else if(password.isEmpty()) {
            view.showCredentialsError(LoginView.LoginCredentialType.PASSWORD,
                    ResourceProvider.getString(R.string.error_password_empty));
            return false;
        }

        return true;
    }

    @Override
    public void onSignUpClicked() {
        view.showSignUpScreen();
    }

    @Override
    public void automaticLogin() {
        if(!SystemStateProvider.hasInternetConnection()) {
            return;
        }

        UserDb user = SQLite.select().from(UserDb.class).querySingle();

        if(user != null) {
            view.fillEmailAndPassword(user.getEmail(), user.getPassword());
            onLoginClicked(new LoginUser(user.getEmail(), user.getPassword()));
        }
    }
}
