package com.ssudio.sfr.components.ui;

import com.ssudio.sfr.MainActivity;
import com.ssudio.sfr.SplashScreenActivity;
import com.ssudio.sfr.modules.DashboardModule;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.NetworkModule;
import com.ssudio.sfr.modules.RegistrationModule;
import com.ssudio.sfr.modules.ReportModule;
import com.ssudio.sfr.modules.SFRApplicationModule;
import com.ssudio.sfr.modules.SplashScreenModule;
import com.ssudio.sfr.payment.component.PaymentComponent;
import com.ssudio.sfr.payment.module.PaymentModule;
import com.ssudio.sfr.registration.presenter.RegistrationPresenter;
import com.ssudio.sfr.ui.ReportFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        SFRApplicationModule.class,
        LocalStorageModule.class,
        LocalAuthenticationModule.class,
        NetworkModule.class
})
public interface BasePresenterComponent {
    SplashScreenComponent newSplashScreenSubComponent(SplashScreenModule splashScreenModule);
    RegistrationComponents newRegistrationSubComponent(RegistrationModule registrationModule);
    DashboardComponents newDashboardSubComponent(DashboardModule dashboardModule);
    ReportComponents newReportSubComponent(ReportModule dashboardModule);
    PaymentComponent newPaymentSubComponent(PaymentModule paymentModule);

    void inject(RegistrationPresenter registrationPresenter);
    void inject(MainActivity mainActivity);
}
