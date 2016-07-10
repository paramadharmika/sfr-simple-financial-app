package com.ssudio.sfr.payment.module;

import com.google.gson.Gson;
import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.payment.command.GetChannelMemberCommand;
import com.ssudio.sfr.payment.command.GetPaymentChannelCommand;
import com.ssudio.sfr.payment.command.IGetChannelMemberCommand;
import com.ssudio.sfr.payment.command.IGetPaymentChannelCommand;
import com.ssudio.sfr.payment.command.ISavePaymentChannelCommand;
import com.ssudio.sfr.payment.command.SavePaymentChannelCommand;
import com.ssudio.sfr.payment.presenter.PaymentPresenter;
import com.ssudio.sfr.payment.ui.IPaymentView;
import com.ssudio.sfr.scope.UserScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class PaymentModule {
    private IPaymentView view;
    private final ILoadingView loadingView;
    private final IConnectivityListenerView connectivityListenerView;

    public PaymentModule(IPaymentView view,
                         IConnectivityListenerView connectivityListenerView,
                         ILoadingView loadingView) {
        this.view = view;
        this.connectivityListenerView = connectivityListenerView;
        this.loadingView = loadingView;
    }

    @Provides
    @UserScope
    public PaymentPresenter getPresenter(IPaymentView view,
                                         IConnectivityListenerView connectivityListenerView,
                                         ILoadingView loadingView,
                                         IGetPaymentChannelCommand getPaymentChannelCommand,
                                         ISavePaymentChannelCommand savePaymentChannelCommand,
                                         IGetChannelMemberCommand getChannelMemberCommand,
                                         LocalAuthenticationService localAuthenticationService) {

        PaymentPresenter presenter = new PaymentPresenter(view,
                connectivityListenerView,
                loadingView,
                getPaymentChannelCommand,
                savePaymentChannelCommand,
                getChannelMemberCommand,
                localAuthenticationService);

        return presenter;
    }

    @Provides
    @UserScope
    public IPaymentView getPaymentView() {
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
    public IGetPaymentChannelCommand getPaymentChannelCommand(OkHttpClient client, Gson gson,
                                                       IAppConfiguration appConfiguration) {
        return new GetPaymentChannelCommand(client, gson, appConfiguration);
    }

    @Provides
    @UserScope
    public IGetChannelMemberCommand getChannelMemberCommand(OkHttpClient client, Gson gson,
                                                              IAppConfiguration appConfiguration) {
        return new GetChannelMemberCommand(client, gson, appConfiguration);
    }

    @Provides
    @UserScope
    public ISavePaymentChannelCommand getSavePaymentCommand(OkHttpClient client, Gson gson,
                                                         IAppConfiguration appConfiguration) {
        return new SavePaymentChannelCommand(client, gson, appConfiguration);
    }
}
