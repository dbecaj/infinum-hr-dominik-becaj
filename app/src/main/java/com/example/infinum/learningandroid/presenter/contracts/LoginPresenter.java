package com.example.infinum.learningandroid.presenter.contracts;

import com.example.infinum.learningandroid.model.LoginUser;

/**
 * Created by infinum on 02/08/2017.
 */

public interface LoginPresenter {

    void automaticLogin();
    void handleUnfinishedNetworkCalls();
    void onLoginClicked(LoginUser user);
    boolean checkInputs(String email, String password);
    void onSignUpClicked();

}
