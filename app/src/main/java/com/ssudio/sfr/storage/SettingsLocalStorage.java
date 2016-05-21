package com.ssudio.sfr.storage;

import android.content.Context;

import com.ssudio.sfr.SFRApplication;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class SettingsLocalStorage {
    private String _filename = "settings-sfr.data";

    private SFRApplication application;

    public SettingsLocalStorage(SFRApplication application) {
        this.application = application;
    }

    public void saveSettingsData(SettingsData settings) {
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(application.openFileOutput(_filename, Context.MODE_PRIVATE));

            out.writeObject(settings);

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SettingsData getSettingsData() {
        ObjectInput objectInput;

        try {
            objectInput = new ObjectInputStream(application.openFileInput(_filename));

            SettingsData localStorageData = (SettingsData)objectInput.readObject();

            objectInput.close();

            return localStorageData;
        } catch (IOException e) {
            //usually file not found exception, when loading for the first time
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new SettingsData();
    }
}
