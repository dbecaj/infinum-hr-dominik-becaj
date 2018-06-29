package com.example.infinum.learningandroid.model.interactors.contracts;

import com.example.infinum.learningandroid.model.LoginUser;
import com.example.infinum.learningandroid.model.SignUpUser;
import com.example.infinum.learningandroid.model.User;

import moe.banana.jsonapi2.ObjectDocument;
import retrofit2.Call;

/**
 * Created by infinum on 02/08/2017.
 */

public interface UserInteractor {

    interface UserListener {
        void onSuccess(User user);
        void onError(String error);
    }

    Call<ObjectDocument<User>> loginUser(LoginUser user, UserListener listener);
    Call<ObjectDocument<User>> signUpUser(SignUpUser user, UserListener listener);
    boolean cacheLogin(LoginUser user);
    void clearCacheLogin();
    Call<ObjectDocument<User>> getUser(String id, UserListener listener);

}
