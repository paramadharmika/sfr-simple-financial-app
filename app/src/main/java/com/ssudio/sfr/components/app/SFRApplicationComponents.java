package com.ssudio.sfr.components.app;

import com.ssudio.sfr.SFRApplication;
import com.ssudio.sfr.modules.SFRApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SFRApplicationModule.class})
public interface SFRApplicationComponents {
    SFRApplication provideApplication();

    /*void inject(SFRApplication application);*/
}
