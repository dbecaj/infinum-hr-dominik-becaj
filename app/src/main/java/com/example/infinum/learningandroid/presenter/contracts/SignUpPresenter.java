package com.example.infinum.learningandroid.presenter.contracts;

import com.example.infinum.learningandroid.model.SignUpUser;

/**
 * Created by infinum on 04/08/2017.
 */

public interface SignUpPresenter {

    void handleUnfinishedNetworkCalls();
    boolean checkInputs(String email, String nickname, String password, String confirmPassword);

    void signUpUser(SignUpUser user);

}
