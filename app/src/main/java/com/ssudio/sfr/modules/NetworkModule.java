package com.ssudio.sfr.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ssudio.sfr.SFRApplication;
import com.ssudio.sfr.configuration.AppConfiguration;
import com.ssudio.sfr.configuration.IAppConfiguration;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Module
public class NetworkModule {
//    String mBaseUrl;

    // Constructor needs one parameter to instantiate.
    public NetworkModule() {
//        this.mBaseUrl = baseUrl;
    }

//    // Dagger will only look for methods annotated with @Provides
//    @Provides
//    @Singleton
//    // Application reference must come from AppModule.class
//    SharedPreferences providesSharedPreferences(Application application) {
//        return PreferenceManager.getDefaultSharedPreferences(application);
//    }

    /*@Provides
    @Singleton
    Cache provideOkHttpCache(SFRApplication application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB

        Cache cache = new Cache(application.getCacheDir(), cacheSize);

        return cache;
    }*/

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

//        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() { //Cache cache
        OkHttpClient client = new OkHttpClient();

        return client;
    }

    @Provides
    @Singleton
    public IAppConfiguration getAppConfiguration() {
        return new AppConfiguration();
    }

//    @Provides
//    @Singleton
//    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .baseUrl(mBaseUrl)
//                .client(okHttpClient)
//                .build();
//        return retrofit;
//    }
}