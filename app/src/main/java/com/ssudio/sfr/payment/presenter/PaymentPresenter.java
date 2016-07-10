package com.ssudio.sfr.payment.presenter;

import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.request.SFRGeneralPostParameter;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.payment.command.IGetChannelMemberCommand;
import com.ssudio.sfr.payment.command.IGetPaymentChannelCommand;
import com.ssudio.sfr.payment.command.ISavePaymentChannelCommand;
import com.ssudio.sfr.payment.event.APIPaymentProgressEvent;
import com.ssudio.sfr.payment.event.GetChannelMemberEvent;
import com.ssudio.sfr.payment.event.GetPaymentsEvent;
import com.ssudio.sfr.payment.event.SavePaymentEvent;
import com.ssudio.sfr.payment.model.PreferredPaymentModel;
import com.ssudio.sfr.payment.ui.IPaymentView;
import com.ssudio.sfr.registration.model.UserModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PaymentPresenter implements IPaymentPresenter {
    private IPaymentView paymentView;
    private IConnectivityListenerView connectivityListenerView;
    private ILoadingView loadingView;
    private IGetPaymentChannelCommand getPaymentChannelCommand;
    private ISavePaymentChannelCommand savePaymentChannelCommand;
    private IGetChannelMemberCommand getChannelMemberCommand;
    private LocalAuthenticationService localAuthenticationService;

    public PaymentPresenter(IPaymentView paymentView,
                            IConnectivityListenerView connectivityListenerView,
                            ILoadingView loadingView,
                            IGetPaymentChannelCommand getPaymentChannelCommand,
                            ISavePaymentChannelCommand savePaymentChannelCommand,
                            IGetChannelMemberCommand getChannelMemberCommand,
                            LocalAuthenticationService localAuthenticationService) {
        this.paymentView = paymentView;
        this.connectivityListenerView = connectivityListenerView;
        this.loadingView = loadingView;
        this.getPaymentChannelCommand = getPaymentChannelCommand;
        this.savePaymentChannelCommand = savePaymentChannelCommand;
        this.getChannelMemberCommand = getChannelMemberCommand;
        this.localAuthenticationService = localAuthenticationService;

        EventBus.getDefault().register(this);
    }

    @Override
    public void getPayments() {
        getPaymentChannelCommand.executeAsync(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPaymentChannelEvent(GetPaymentsEvent e) {
        if (e.getIsSuccess()) {
            paymentView.bindPayments(e.getPayments());
        } else {
            paymentView.showMessage(e.getIsSuccess(), e.getMessage());
        }
    }

    @Override
    public void getMemberPaymentChannel() {
        String verificationCode = getVerificationCode();

        if (!verificationCode.isEmpty()) {
            getChannelMemberCommand.executeAsync(new SFRGeneralPostParameter(verificationCode));
        } else {
            paymentView.showMessage(false, "Unable to find verification code");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChannelMemberEvent(GetChannelMemberEvent e) {
        if (e.getIsSuccess()) {
            
        } else {
            paymentView.showMessage(false, e.getMessage());
        }
    }

    @Override
    public void savePayment(int channelId, double amount) {
        String verificationCode = getVerificationCode();

        if (!verificationCode.isEmpty()) {
            PreferredPaymentModel model = new PreferredPaymentModel(verificationCode);

            model.setIdChannelMember(channelId);
            model.setAmount(amount);

            savePaymentChannelCommand.executeAsync(model);
        } else {
            paymentView.showMessage(false, "Unable to find verification code");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSavePaymentEvent(SavePaymentEvent e) {
        paymentView.showMessage(e.getIsSuccess(), e.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAPIProgressEvent(APIPaymentProgressEvent e) {
        if (e.isLoading()) {
            loadingView.showLoading();
        } else {
            loadingView.dismissLoading();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(NetworkConnectivityEvent e) {
        connectivityListenerView.showMessage(e);
    }

    @Override
    public void unregisterEventHandler() {
        EventBus.getDefault().unregister(this);
    }

    private String getVerificationCode() {
        UserModel user = localAuthenticationService.getLocalAuthenticatedUser();

        if (user == null) {
            return "";
        }

        String verificationCode = user.getVerificationCode();

        if (verificationCode == null || verificationCode.isEmpty()) {
            return "";
        }

        return verificationCode;
    }
}
