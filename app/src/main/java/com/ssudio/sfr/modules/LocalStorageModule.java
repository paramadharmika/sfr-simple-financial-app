package com.ssudio.sfr.modules;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ssudio.sfr.SFRApplication;
import com.ssudio.sfr.storage.SettingsLocalStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalStorageModule {
    @Provides
    @Singleton
    public SettingsLocalStorage getSettingsLocalStorage(SFRApplication application) {
        return new SettingsLocalStorage(application);
    }

    @Provides
    @Singleton
    public SharedPreferences sharedPreferences(SFRApplication application) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(application);

        return sharedPreferences;
    }
}
