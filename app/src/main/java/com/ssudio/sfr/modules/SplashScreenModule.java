package com.ssudio.sfr.modules;

import com.google.gson.Gson;
import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.scope.UserScope;
import com.ssudio.sfr.splashscreen.command.ISplashScreenCommand;
import com.ssudio.sfr.splashscreen.command.SplashScreenCommand;
import com.ssudio.sfr.splashscreen.presenter.ISplashScreenView;
import com.ssudio.sfr.splashscreen.presenter.SplashScreenPresenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

//todo: rename this to be SplashScreenPresenterModule
@Module
public class SplashScreenModule {
    private ISplashScreenView view;

    public SplashScreenModule(ISplashScreenView view) {
        this.view = view;
    }

    @Provides
    @UserScope
    public SplashScreenPresenter getPresenter(ISplashScreenView view,
                                               ISplashScreenCommand command) {
        SplashScreenPresenter presenter = new SplashScreenPresenter(view);

        presenter.setGetSplashScreenAppInfoCommand(command);

        return presenter;
    }

    @Provides
    @UserScope
    public ISplashScreenView getSplashScreenView() {
        return this.view;
    }

    @Provides
    @UserScope
    public ISplashScreenCommand getSplashScreenCommand(OkHttpClient client, Gson gson,
                                                       IAppConfiguration appConfiguration) {
        return new SplashScreenCommand(client, gson, appConfiguration);
    }
}
