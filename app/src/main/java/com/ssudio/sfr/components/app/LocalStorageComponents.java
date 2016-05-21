package com.ssudio.sfr.components.app;

import android.content.SharedPreferences;

import com.ssudio.sfr.SFRApplication;
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

    //todo: interesting here, how to provide to local authentication component, since local auth component is UserScope
    //void inject(SFRApplication app);
}
