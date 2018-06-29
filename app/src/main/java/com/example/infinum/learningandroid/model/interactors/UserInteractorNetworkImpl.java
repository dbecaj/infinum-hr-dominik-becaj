package com.example.infinum.learningandroid.model.interactors;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.LoginUser;
import com.example.infinum.learningandroid.model.SignUpUser;
import com.example.infinum.learningandroid.model.User;
import com.example.infinum.learningandroid.model.UserDb;
import com.example.infinum.learningandroid.model.api.UserApi;
import com.example.infinum.learningandroid.model.interactors.contracts.UserInteractor;
import com.example.infinum.learningandroid.network.RestService;
import com.example.infinum.learningandroid.utils.ResourceProvider;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import moe.banana.jsonapi2.ObjectDocument;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by infinum on 02/08/2017.
 */

public class UserInteractorNetworkImpl implements UserInteractor {

    @Override
    public Call<ObjectDocument<User>> loginUser(LoginUser user, final UserListener listener) {
        ObjectDocument<LoginUser> document = new ObjectDocument<>();
        document.set(user);

        UserApi userApi = RestService.getRestApi().create(UserApi.class);

        Call<ObjectDocument<User>> call = userApi.loginUser(document);
        call.enqueue(new Callback<ObjectDocument<User>>() {
            @Override
            public void onResponse(Call<ObjectDocument<User>> call,
                                   Response<ObjectDocument<User>> response) {
                if(!response.isSuccessful()) {
                    listener.onError(ResourceProvider.getString(R.string.error_invalid_credentials));
                    return;
                }

                listener.onSuccess(response.body().get());
            }

            @Override
            public void onFailure(Call<ObjectDocument<User>> call, Throwable t) {
                listener.onError(ResourceProvider.getString(R.string.error_failure_to_login));
                Timber.d(t.getMessage());
            }
        });

        return call;
    }

    @Override
    public Call<ObjectDocument<User>> signUpUser(SignUpUser user, final UserListener listener) {
        ObjectDocument<SignUpUser> document = new ObjectDocument<>();
        document.set(user);

        UserApi userApi = RestService.getRestApi().create(UserApi.class);

        Call<ObjectDocument<User>> call = userApi.signUpUser(document);
        call.enqueue(new Callback<ObjectDocument<User>>() {
            @Override
            public void onResponse(Call<ObjectDocument<User>> call, Response<ObjectDocument<User>> response) {
                if(!response.isSuccessful()) {
                    listener.onError(ResourceProvider.getString(R.string.error_can_not_signup));
                    return;
                }

                listener.onSuccess(response.body().get());
            }

            @Override
            public void onFailure(Call<ObjectDocument<User>> call, Throwable t) {
                listener.onError(ResourceProvider.getString(R.string.error_can_not_signup));
                Timber.d(t.getMessage());
            }
        });

        return call;
    }

    @Override
    public boolean cacheLogin(LoginUser user) {
        clearCacheLogin();

        UserDb userDb = new UserDb(user.getEmail(), user.getPassword());

        return userDb.save();
    }

    @Override
    public void clearCacheLogin() {
        SQLite.delete().from(UserDb.class).execute();
    }

    @Override
    public Call<ObjectDocument<User>> getUser(String id, final UserListener listener) {
        UserApi userApi = RestService.getRestApi().create(UserApi.class);

        Call<ObjectDocument<User>> call = userApi.getUser(id);
        call.enqueue(new Callback<ObjectDocument<User>>() {
            @Override
            public void onResponse(Call<ObjectDocument<User>> call,
                                   Response<ObjectDocument<User>> response) {
                if(!response.isSuccessful()) {
                    listener.onError(ResourceProvider.getString(R.string.error_could_not_find_user));
                    return;
                }

                listener.onSuccess(response.body().get());
            }

            @Override
            public void onFailure(Call<ObjectDocument<User>> call, Throwable t) {
                listener.onError(ResourceProvider.getString(R.string.error_could_not_get_user));
                Timber.d(t.getMessage());
            }
        });

        return call;
    }
}
