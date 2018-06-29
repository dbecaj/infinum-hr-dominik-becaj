package com.example.infinum.learningandroid.network;

import com.example.infinum.learningandroid.MyApplication;
import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.LoginUser;
import com.example.infinum.learningandroid.model.PokemonJson;
import com.example.infinum.learningandroid.model.User;
import com.example.infinum.learningandroid.model.api.JsonApiConverterFactory;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.ResourceAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

/**
 * Created by infinum on 26/07/2017.
 */

public class RestService {

    private static Retrofit retrofit;

    public static Retrofit getRestApi() {
        if(retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                    new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Timber.d(message);
                        }
                    });
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(interceptor).build();

            JsonAdapter.Factory jsonAdapterFactory = ResourceAdapterFactory.builder()
                    .add(LoginUser.class)
                    .add(User.class)
                    .add(PokemonJson.class)
                    .build();
            Moshi moshi = new Moshi.Builder().add(jsonAdapterFactory).build();

            retrofit = new Retrofit.Builder().baseUrl(MyApplication.getInstance().getString(R.string.api_url))
                    .addConverterFactory(JsonApiConverterFactory.create(moshi))
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .client(client)
                    .build();
        }

        return retrofit;
    }

}
