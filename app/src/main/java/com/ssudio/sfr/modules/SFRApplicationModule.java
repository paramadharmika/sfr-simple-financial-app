package com.ssudio.sfr.modules;

import com.ssudio.sfr.SFRApplication;
import com.ssudio.sfr.configuration.AppConfiguration;
import com.ssudio.sfr.configuration.IAppConfiguration;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SFRApplicationModule {
    private SFRApplication application;

    public SFRApplicationModule(SFRApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public SFRApplication getApplication() {
        return this.application;
    }
}
