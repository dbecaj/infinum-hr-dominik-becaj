package com.example.infinum.learningandroid.model.interactors.contracts;

/**
 * Created by infinum on 05/08/2017.
 */

public interface StorageInteractor {

    interface StorageListener {
        void onSuccess(String imagePath);
        void onError(String error);
    }

    void saveImage(byte[] imageData, StorageListener listener);

}
