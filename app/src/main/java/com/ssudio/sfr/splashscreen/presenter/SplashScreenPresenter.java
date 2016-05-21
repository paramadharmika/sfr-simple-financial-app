package com.ssudio.sfr.splashscreen.presenter;

import com.ssudio.sfr.splashscreen.command.ISplashScreenCommand;
import com.ssudio.sfr.splashscreen.event.SplashScreenEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

public class SplashScreenPresenter implements ISplashScreenPresenter {
    private ISplashScreenView view;
    private ISplashScreenCommand getSplashScreenAppInfoCommand;

    @Inject
    public SplashScreenPresenter(ISplashScreenView view) {
        this.view = view;

        EventBus.getDefault().register(this);
    }

    @Override
    public void getAppInfoAsync() {
        getSplashScreenAppInfoCommand.executeAsync(null);
    }

    @Subscribe
    public void onSplashScreenEvent(SplashScreenEvent e) {
        view.bindSplashScreenInfo(e.getModel());
    }

    @Override
    public void unregisterEventHandler() {
        EventBus.getDefault().unregister(this);
    }

    public void setGetSplashScreenAppInfoCommand(ISplashScreenCommand getSplashScreenAppInfoCommand) {
        this.getSplashScreenAppInfoCommand = getSplashScreenAppInfoCommand;
    }
}
