package com.ssudio.sfr.components.app;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

@Singleton
@Component(modules = {NetworkModule.class}) //SFRApplicationModule.class,
public interface NetworkComponents {
    IAppConfiguration provideAppConfiguration();
    /*Cache provideCache();*/
    Gson provideGson();
    OkHttpClient provideOkHttpClient();
}
