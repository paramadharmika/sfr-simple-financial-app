package com.ssudio.sfr.components.ui;

import com.ssudio.sfr.SFRApplication;
import com.ssudio.sfr.SplashScreenActivity;
import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.components.app.LocalAuthenticationComponent;
import com.ssudio.sfr.components.app.NetworkComponents;
import com.ssudio.sfr.components.app.SFRApplicationComponents;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.SFRApplicationModule;
import com.ssudio.sfr.modules.SplashScreenModule;
import com.ssudio.sfr.scope.UserScope;
import com.ssudio.sfr.splashscreen.presenter.SplashScreenPresenter;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;

@UserScope
@Subcomponent(
        modules = {SplashScreenModule.class /*, LocalAuthenticationModule.class*/}
        /*dependencies = {BasePresenterComponent.class}*/
)
public interface SplashScreenComponent {
    SplashScreenPresenter provideSplashScreenPresenter();
    /*LocalAuthenticationService provideLocalAuthenticationService();*/

    //injecting must be on concrete class
    void inject(SplashScreenActivity splashScreenActivity);
}
