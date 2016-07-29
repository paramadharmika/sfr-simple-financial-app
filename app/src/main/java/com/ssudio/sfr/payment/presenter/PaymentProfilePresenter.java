package com.ssudio.sfr.payment.presenter;

import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.request.SFRGeneralPostParameter;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.payment.command.IGetChannelMemberCommand;
import com.ssudio.sfr.payment.command.IGetPaymentChannelCommand;
import com.ssudio.sfr.payment.event.APIPaymentProgressEvent;
import com.ssudio.sfr.payment.event.GetMemberPaymentMethodEvent;
import com.ssudio.sfr.payment.event.GetPaymentsEvent;
import com.ssudio.sfr.payment.event.SavePaymentEvent;
import com.ssudio.sfr.payment.model.MemberPaymentModel;
import com.ssudio.sfr.payment.command.ISavePaymentProfileCommand;
import com.ssudio.sfr.payment.ui.IPaymentProfileView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class PaymentProfilePresenter implements IPaymentProfilePresenter  {
    private final IPaymentProfileView paymentView;
    private final IConnectivityListenerView connectivityListenerView;
    private final ILoadingView loadingView;
    private final IGetPaymentChannelCommand getPaymentChannelCommand;
    private final IGetChannelMemberCommand getChannelMemberCommand;
    private final ISavePaymentProfileCommand savePaymentProfileCommand;
    private final LocalAuthenticationService localAuthenticationService;
    private ArrayList<MemberPaymentModel> memberPaymentMethods;

    private boolean isInitializingPaymentProfile = false;

    public PaymentProfilePresenter(IPaymentProfileView view,
                                   IConnectivityListenerView connectivityListenerView,
                                   ILoadingView loadingView,
                                   IGetPaymentChannelCommand getPaymentChannelCommand,
                                   ISavePaymentProfileCommand savePaymentProfileCommand,
                                   IGetChannelMemberCommand getChannelMemberCommand,
                                   LocalAuthenticationService localAuthenticationService) {
        this.paymentView = view;
        this.connectivityListenerView = connectivityListenerView;
        this.loadingView = loadingView;
        this.getPaymentChannelCommand = getPaymentChannelCommand;
        this.savePaymentProfileCommand = savePaymentProfileCommand;
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
    public void getMemberPaymentMethods() {
        String verificationCode = localAuthenticationService.getLocalVerificationCode();

        if (!verificationCode.isEmpty()) {
            getChannelMemberCommand.executeAsync(new SFRGeneralPostParameter(verificationCode));
        } else {
            paymentView.showMessage(false, "Unable to find verification code");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChannelMemberEvent(GetMemberPaymentMethodEvent e) {
        if (e.getIsSuccess()) {
            memberPaymentMethods = e.getModels();
        } else {
            memberPaymentMethods = new ArrayList<>();

            paymentView.showMessage(false, e.getMessage());
        }
    }

    @Override
    public void savePaymentProfile(int channelId, int idChannelMember, String paymentName, String paymentTo) {
        String verificationCode = localAuthenticationService.getLocalVerificationCode();

        if (!verificationCode.isEmpty()) {
            MemberPaymentModel existingMemberPaymentMethod = anyMemberPaymentModel(channelId, idChannelMember);

            MemberPaymentModel model = new MemberPaymentModel(verificationCode);

            idChannelMember = existingMemberPaymentMethod == null ?
                    idChannelMember : existingMemberPaymentMethod.getIdChannelMember();

            model.setIdChannel(channelId);
            model.setIdChannelMember(idChannelMember);
            model.setPaymentName(paymentName);
            model.setPaymentTo(paymentTo);

            savePaymentProfileCommand.executeAsync(model);
        } else {
            paymentView.showMessage(false, "Unable to find verification code");
        }
    }

    private MemberPaymentModel anyMemberPaymentModel(int channelId, int idChannelMember) {
        MemberPaymentModel memberPaymentModel = null;

        for (MemberPaymentModel paymentMethod: memberPaymentMethods) {
            if (paymentMethod.getIdChannel() == channelId) {
                memberPaymentModel = paymentMethod;

                break;
            }
        }

        return memberPaymentModel;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSavePaymentEvent(SavePaymentEvent e) {
        if (!isInitializingPaymentProfile) {
            paymentView.showMessage(e.getIsSuccess(), e.getMessage());

            getMemberPaymentMethods();
        } else {
            if (e.getIsSuccess()) {
                paymentView.showMainActivity();
            } else {
                paymentView.showMessage(e.getIsSuccess(), e.getMessage());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAPIProgressEvent(APIPaymentProgressEvent e) {
        boolean isLoading = e.isLoading();

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

    public void setInitializingPaymentProfile(boolean initializingPaymentProfile) {
        isInitializingPaymentProfile = initializingPaymentProfile;
    }
}
