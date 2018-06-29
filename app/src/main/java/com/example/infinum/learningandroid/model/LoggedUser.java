package com.example.infinum.learningandroid.model;

import com.example.infinum.learningandroid.model.interactors.UserInteractorNetworkImpl;

/**
 * Created by infinum on 02/08/2017.
 */

public class LoggedUser {

    private static User loggedUser;

    protected LoggedUser() {

    }

    public static boolean isUserLoggedIn() {
        return loggedUser != null;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void cacheUser(LoginUser user) {
        UserInteractorNetworkImpl interactor = new UserInteractorNetworkImpl();
        interactor.cacheLogin(user);
    }

    public static void loginUser(User user) {
        loggedUser = user;
    }

    public static boolean logoutUser() {
        if(loggedUser != null) {
            UserInteractorNetworkImpl interactor = new UserInteractorNetworkImpl();
            interactor.clearCacheLogin();
            loggedUser = null;
            return true;
        }
        else {
            return false;
        }
    }

}
