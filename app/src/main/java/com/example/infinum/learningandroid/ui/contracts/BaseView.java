package com.example.infinum.learningandroid.ui.contracts;

/**
 * Created by infinum on 02/08/2017.
 */

public interface BaseView {

    void initUi();
    void showProgress();
    void hideProgress();
    void showMessage(String message);

}
