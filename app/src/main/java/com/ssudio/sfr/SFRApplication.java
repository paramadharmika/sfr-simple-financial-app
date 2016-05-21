package com.ssudio.sfr;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ssudio.sfr.components.app.DaggerLocalAuthenticationComponent;
import com.ssudio.sfr.components.app.DaggerLocalStorageComponents;
import com.ssudio.sfr.components.app.DaggerSFRApplicationComponents;
import com.ssudio.sfr.components.app.LocalAuthenticationComponent;
import com.ssudio.sfr.components.app.LocalStorageComponents;
import com.ssudio.sfr.components.app.SFRApplicationComponents;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.SFRApplicationModule;
import com.ssudio.sfr.storage.SettingsLocalStorage;

import javax.inject.Inject;

public class SFRApplication extends Application {
    private SFRApplicationComponents sfrApplicationComponents;
    private SFRApplicationModule sfrApplicationModule;

    /*private LocalStorageComponents localStorageComponents;
    private LocalAuthenticationComponent localAuthenticationComponent;

    @Inject
    SettingsLocalStorage localStorage;*/

    @Override
    public void onCreate() {
        super.onCreate();

        sfrApplicationModule = new SFRApplicationModule(this);

        sfrApplicationComponents = DaggerSFRApplicationComponents.builder()
                .sFRApplicationModule(sfrApplicationModule)
                .build();

        /*localStorageComponents = DaggerLocalStorageComponents.builder()
                .sFRApplicationModule(sfrApplicationModule)
                .localStorageModule(new LocalStorageModule())
                .build();

        localAuthenticationComponent = DaggerLocalAuthenticationComponent.builder()
                .localStorageModule(new LocalStorageModule())
                .localAuthenticationModule(new LocalAuthenticationModule())
                .build();*/

        /*sfrApplicationComponents.inject(this);*/

        /*sfrLocalStorageComponents = DaggerLocalStorageComponents.builder()
                .sFRApplicationModule(sfrApplicationModule)
                .localStorageModule(new LocalStorageModule())
                .build();

        sfrLocalStorageComponents.inject(this);*/


        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config); // Do it on Application start
    }

    public SFRApplicationComponents getSfrApplicationComponents() {
        return sfrApplicationComponents;
    }

    public SFRApplicationModule getSfrApplicationModule() {
        return sfrApplicationModule;
    }

    /*public SettingsLocalStorage getLocalStorage() {
        return localStorage;
    }*/
}
