package com.ssudio.sfr.splash.presenter;

import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.splashscreen.command.ISplashScreenCommand;
import com.ssudio.sfr.splashscreen.model.SplashScreenModel;
import com.ssudio.sfr.splashscreen.presenter.ISplashScreenView;
import com.ssudio.sfr.splashscreen.presenter.SplashScreenPresenter;

import org.junit.Test;

public class SplashScreenPresenterTest {
    @Test
    public void splashScreenPresenter_should() {
        SplashScreenPresenter presenter = new SplashScreenPresenter(new ISplashScreenView() {
            @Override
            public void bindSplashScreenInfo(SplashScreenModel model) {

            }
        }, new IConnectivityListenerView() {
            @Override
            public void showMessage(NetworkConnectivityEvent e) {

            }
        });

        presenter.setGetSplashScreenAppInfoCommand(new ISplashScreenCommand() {
            @Override
            public void executeAsync(Void param) {

            }
        });

        presenter.getAppInfoAsync();
    }
}
