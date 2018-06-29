package com.example.infinum.learningandroid.model.interactors;

import com.example.infinum.learningandroid.model.interactors.contracts.StorageInteractor;
import com.example.infinum.learningandroid.model.storage.SystemStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by infinum on 05/08/2017.
 */

public class StorageInteractorImpl implements StorageInteractor {

    @Override
    public void saveImage(byte[] imageData, StorageListener listener) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());

        File imageFile = new File(SystemStorage.getImageDir(), currentDateAndTime);
        FileOutputStream fOS = null;
        try {
            fOS = new FileOutputStream(imageFile);
            fOS.write(imageData);
            listener.onSuccess(imageFile.getAbsolutePath());
        }
        catch(Exception e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
        finally {
            if(fOS != null) {
                try {
                    fOS.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    listener.onError(e.getMessage());
                }
            }
        }
    }
}
