package com.example.infinum.learningandroid.presenter;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.LoggedUser;
import com.example.infinum.learningandroid.model.SignUpUser;
import com.example.infinum.learningandroid.model.User;
import com.example.infinum.learningandroid.model.interactors.UserInteractorNetworkImpl;
import com.example.infinum.learningandroid.model.interactors.contracts.UserInteractor;
import com.example.infinum.learningandroid.presenter.contracts.SignUpPresenter;
import com.example.infinum.learningandroid.ui.contracts.SignUpView;
import com.example.infinum.learningandroid.utils.AppUtils;
import com.example.infinum.learningandroid.utils.ResourceProvider;
import com.example.infinum.learningandroid.utils.SystemStateProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moe.banana.jsonapi2.ObjectDocument;
import retrofit2.Call;

/**
 * Created by infinum on 04/08/2017.
 */

public class SignUpPresenterImpl implements SignUpPresenter {

    private SignUpView view;
    Call<ObjectDocument<User>> call;

    public SignUpPresenterImpl(SignUpView view) {
        this.view = view;
    }

    @Override
    public void handleUnfinishedNetworkCalls() {
        if(call != null) {
            call.cancel();
        }
    }

    @Override
    public boolean checkInputs(String email, String nickname, String password, String confirmPassword) {
        if(email.isEmpty()) {
            view.showCredentialError(SignUpView.SignUpCredentialType.EMAIL,
                    ResourceProvider.getString(R.string.error_email_empty));
            return false;
        }
        else if(!AppUtils.isEmailValid(email)) {
            view.showCredentialError(SignUpView.SignUpCredentialType.EMAIL,
                    ResourceProvider.getString(R.string.error_not_email));
            return false;
        }
        else if(nickname.isEmpty()) {
            view.showCredentialError(SignUpView.SignUpCredentialType.NICKNAME,
                    ResourceProvider.getString(R.string.error_nickname_empty));
            return false;
        }
        else if(password.isEmpty()) {
            view.showCredentialError(SignUpView.SignUpCredentialType.PASSWORD,
                    ResourceProvider.getString(R.string.error_password_empty));
            return false;
        }
        else if(password.length() < ResourceProvider.getInt(R.integer.minimum_password_length)) {
            view.showCredentialError(SignUpView.SignUpCredentialType.PASSWORD,
                    ResourceProvider.getString(R.string.error_password_length));
            return false;
        }
        else if(!password.equals(confirmPassword)) {
            view.showCredentialError(SignUpView.SignUpCredentialType.CONFIRM_PASSWORD,
                    ResourceProvider.getString(R.string.error_password_not_match));
            return false;
        }

        return true;
    }

    @Override
    public void signUpUser(SignUpUser user) {
        if(!SystemStateProvider.hasInternetConnection()) {
            view.showMessage(ResourceProvider.getString(R.string.error_no_internet));
            return;
        }

        if(!checkInputs(user.getEmail(), user.getUsername(), user.getPassword(),
                user.getConfirmPassword())) {
            return;
        }

        UserInteractorNetworkImpl interactor = new UserInteractorNetworkImpl();
        view.showProgress();
        interactor.signUpUser(user, new UserInteractor.UserListener() {
            @Override
            public void onSuccess(User user) {
                view.hideProgress();
                LoggedUser.loginUser(user);
                view.showMainScreen();
            }

            @Override
            public void onError(String error) {
                view.hideProgress();
                view.showMessage(error);
            }
        });
    }
}
