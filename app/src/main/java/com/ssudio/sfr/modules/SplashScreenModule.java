package com.ssudio.sfr.modules;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.scope.UserScope;
import com.ssudio.sfr.splashscreen.command.ISplashScreenCommand;
import com.ssudio.sfr.splashscreen.command.SplashScreenCommand;
import com.ssudio.sfr.splashscreen.presenter.ISplashScreenView;
import com.ssudio.sfr.splashscreen.presenter.SplashScreenPresenter;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

//todo: rename this to be SplashScreenPresenterModule
@Module
public class SplashScreenModule {
    private ISplashScreenView view;
    private IConnectivityListenerView connectivityListenerView;

    public SplashScreenModule(ISplashScreenView view, IConnectivityListenerView connectivityListenerView) {
        this.view = view;
        this.connectivityListenerView = connectivityListenerView;
    }

    @Provides
    @UserScope
    public SplashScreenPresenter getPresenter(ISplashScreenView view,
                                              IConnectivityListenerView connectivityListenerView,
                                              ISplashScreenCommand command) {
        SplashScreenPresenter presenter = new SplashScreenPresenter(view, connectivityListenerView);

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
    public IConnectivityListenerView getNetworkListenerView() {
        return this.connectivityListenerView;
    }

    @Provides
    @UserScope
    public ISplashScreenCommand getSplashScreenCommand(OkHttpClient client, Gson gson,
                                                       IAppConfiguration appConfiguration) {
        return new SplashScreenCommand(client, gson, appConfiguration);
    }
}
