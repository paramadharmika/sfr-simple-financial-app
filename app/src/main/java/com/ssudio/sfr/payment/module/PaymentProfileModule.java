package com.ssudio.sfr.payment.module;

import com.google.gson.Gson;
import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.payment.command.IGetChannelMemberCommand;
import com.ssudio.sfr.payment.command.IGetPaymentChannelCommand;
import com.ssudio.sfr.payment.command.ISavePaymentProfileCommand;
import com.ssudio.sfr.payment.command.SavePaymentProfileCommand;
import com.ssudio.sfr.payment.presenter.PaymentProfilePresenter;
import com.ssudio.sfr.payment.ui.IPaymentProfileView;
import com.ssudio.sfr.scope.UserScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class PaymentProfileModule {
    private IPaymentProfileView view;
    private final ILoadingView loadingView;
    private final IConnectivityListenerView connectivityListenerView;

    public PaymentProfileModule(IPaymentProfileView view,
                                IConnectivityListenerView connectivityListenerView,
                                ILoadingView loadingView) {
        this.view = view;
        this.loadingView = loadingView;
        this.connectivityListenerView = connectivityListenerView;
    }

    @Provides
    @UserScope
    public IPaymentProfileView getPaymentView() {
        return this.view;
    }

    @Provides
    @UserScope
    public IConnectivityListenerView getConnectivityListenerView() {
        return this.connectivityListenerView;
    }

    @Provides
    @UserScope
    public ILoadingView getLoadingView() {
        return this.loadingView;
    }

    @Provides
    @UserScope
    public PaymentProfilePresenter getPresenter(IPaymentProfileView view,
                                                IConnectivityListenerView connectivityListenerView,
                                                ILoadingView loadingView,
                                                IGetPaymentChannelCommand getPaymentChannelCommand,
                                                ISavePaymentProfileCommand savePaymentProfileCommand,
                                                IGetChannelMemberCommand getChannelMemberCommand,
                                                LocalAuthenticationService localAuthenticationService) {

        PaymentProfilePresenter presenter = new PaymentProfilePresenter(view,
                connectivityListenerView, loadingView, getPaymentChannelCommand, savePaymentProfileCommand,
                getChannelMemberCommand, localAuthenticationService);

        return presenter;
    }

    @Provides
    @UserScope
    public ISavePaymentProfileCommand getSavePaymentCommand(OkHttpClient client, Gson gson,
                                                           IAppConfiguration appConfiguration) {
        return new SavePaymentProfileCommand(client, gson, appConfiguration);
    }
}
