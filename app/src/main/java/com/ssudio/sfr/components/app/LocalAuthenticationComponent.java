package com.ssudio.sfr.components.app;

import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.SFRApplicationModule;
import com.ssudio.sfr.registration.model.UserModel;
import com.ssudio.sfr.scope.UserScope;
import com.ssudio.sfr.storage.SettingsLocalStorage;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SFRApplicationModule.class, LocalStorageModule.class, LocalAuthenticationModule.class})
public interface LocalAuthenticationComponent {
    LocalAuthenticationService provideLocalAuthenticationService();
    UserModel provideLocalUser();
}
