package com.ssudio.sfr.components.ui;

import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.components.app.NetworkComponents;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.NetworkModule;
import com.ssudio.sfr.modules.RegistrationModule;
import com.ssudio.sfr.modules.SFRApplicationModule;
import com.ssudio.sfr.registration.presenter.RegistrationPresenter;
import com.ssudio.sfr.scope.UserScope;
import com.ssudio.sfr.ui.RegistrationFragment;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;

@UserScope
/*@Component(modules = {RegistrationModule.class, NetModule.class},
        dependencies = {SFRApplicationModule.class, LocalStorageModule.class})*/
@Subcomponent(modules = {RegistrationModule.class}
        /*dependencies = {NetworkComponents.class}*/) //LocalAuthenticationModule.class
public interface RegistrationComponents {
    RegistrationPresenter provideRegistrationPresenter();
    /*LocalAuthenticationService provideLocalAuthenticationService();*/

    //injecting must be on concrete class
    void inject(RegistrationFragment registrationFragment);
}
