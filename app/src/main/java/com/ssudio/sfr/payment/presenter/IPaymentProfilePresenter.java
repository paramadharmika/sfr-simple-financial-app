package com.ssudio.sfr.payment.presenter;

import com.ssudio.sfr.presenter.IEventHandler;

public interface IPaymentProfilePresenter extends IEventHandler {
    void getPayments();
    void getMemberPaymentMethods();
    void savePaymentProfile(int channelId, int idChannelMember, String paymentName, String paymentTo);
}
