package com.ssudio.sfr.modules;

import com.ssudio.sfr.SFRApplication;
import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.components.app.SFRApplicationComponents;
import com.ssudio.sfr.registration.model.UserModel;
import com.ssudio.sfr.scope.UserScope;
import com.ssudio.sfr.storage.SettingsLocalStorage;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalAuthenticationModule {
    /*private final SFRApplication app;
    private final SettingsLocalStorage localStorage;

    @Inject
    public LocalAuthenticationModule(SFRApplication app, SettingsLocalStorage localStorage) {

        this.app = app;
        this.localStorage = localStorage;
    }*/

    @Singleton
    @Provides
    public LocalAuthenticationService getLocalAuthenticationService(SettingsLocalStorage localStorage) {
        return new LocalAuthenticationService(localStorage);
    }

    @Singleton
    @Provides
    public UserModel getLocalAuthenticatedUser(LocalAuthenticationService service) {
        return service.getLocalAuthenticatedUser();
    }

}
