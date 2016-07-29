package com.ssudio.sfr.payment.module;

import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.payment.command.IGetChannelMemberCommand;
import com.ssudio.sfr.payment.command.IGetPaymentChannelCommand;
import com.ssudio.sfr.payment.command.ISavePaymentChannelCommand;
import com.ssudio.sfr.payment.presenter.PaymentRequestPresenter;
import com.ssudio.sfr.payment.ui.IPaymentRequestView;
import com.ssudio.sfr.payment.ui.IPaymentView;
import com.ssudio.sfr.scope.UserScope;

import dagger.Module;
import dagger.Provides;

@Module
public class PaymentRequestModule {
    private IPaymentRequestView view;
    private final ILoadingView loadingView;
    private final IConnectivityListenerView connectivityListenerView;

    public PaymentRequestModule(IPaymentRequestView view,
                         IConnectivityListenerView connectivityListenerView,
                         ILoadingView loadingView) {
        this.view = view;
        this.connectivityListenerView = connectivityListenerView;
        this.loadingView = loadingView;
    }

    @Provides
    @UserScope
    public PaymentRequestPresenter getPresenter(IPaymentRequestView view,
                                                IConnectivityListenerView connectivityListenerView,
                                                ILoadingView loadingView,
                                                IGetPaymentChannelCommand getPaymentChannelCommand,
                                                ISavePaymentChannelCommand savePaymentChannelCommand,
                                                IGetChannelMemberCommand getChannelMemberCommand,
                                                LocalAuthenticationService localAuthenticationService) {

        PaymentRequestPresenter presenter = new PaymentRequestPresenter(view,
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
    public IPaymentRequestView getPaymentView() {
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
}
