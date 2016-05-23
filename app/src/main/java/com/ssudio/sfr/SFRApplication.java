package com.ssudio.sfr;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ssudio.sfr.modules.SFRApplicationModule;

public class SFRApplication extends Application {
    private SFRApplicationModule sfrApplicationModule;

    @Override
    public void onCreate() {
        super.onCreate();

        sfrApplicationModule = new SFRApplicationModule(this);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config); // Do it on Application start
    }

    public SFRApplicationModule getSfrApplicationModule() {
        return sfrApplicationModule;
    }
}
