package com.example.infinum.learningandroid.ui.contracts;

/**
 * Created by infinum on 02/08/2017.
 */

public interface LoginView extends BaseView {

    enum LoginCredentialType {
        EMAIL,
        PASSWORD
    }

    void showCredentialsError(LoginCredentialType type, String error);
    void showMainScreen();
    void showSignUpScreen();
    void showPassword();
    void hidePassword();
    void fillEmailAndPassword(String email, String password);

}
