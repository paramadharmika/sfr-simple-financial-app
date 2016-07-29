package com.ssudio.sfr.payment.presenter;

import com.ssudio.sfr.presenter.IEventHandler;

public interface IPaymentRequestPresenter extends IEventHandler {
    void getPayments();
    void getMemberPaymentChannel();
    void savePayment(int channelId, double amount);
}
