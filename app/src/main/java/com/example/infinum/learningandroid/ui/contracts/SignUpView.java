package com.example.infinum.learningandroid.ui.contracts;

/**
 * Created by infinum on 02/08/2017.
 */

public interface SignUpView extends BaseView {

    enum SignUpCredentialType {
        EMAIL,
        NICKNAME,
        PASSWORD,
        CONFIRM_PASSWORD
    }

    void showCredentialError(SignUpCredentialType type, String error);
    void showMainScreen();

}
