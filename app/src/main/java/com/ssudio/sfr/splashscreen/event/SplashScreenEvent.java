package com.ssudio.sfr.splashscreen.event;

import com.ssudio.sfr.splashscreen.model.SplashScreenModel;

public class SplashScreenEvent {
    private SplashScreenModel model;

    public SplashScreenEvent(SplashScreenModel model) {
        this.model = model;
    }

    public SplashScreenModel getModel() {
        return model;
    }
}
