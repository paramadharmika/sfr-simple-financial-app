package com.ssudio.sfr.components.app;

import android.content.SharedPreferences;

import com.ssudio.sfr.MainActivity;
import com.ssudio.sfr.SFRApplication;
import com.ssudio.sfr.SplashScreenActivity;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.SFRApplicationModule;
import com.ssudio.sfr.storage.SettingsLocalStorage;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SFRApplicationModule.class, LocalStorageModule.class})
public interface LocalStorageComponents {
    SettingsLocalStorage provideSettingsLocalStorage();
    SharedPreferences provideSharedPreferences();

    void inject(MainActivity mainActivity);
    /*void inject(SplashScreenActivity mainActivity);*/
}
