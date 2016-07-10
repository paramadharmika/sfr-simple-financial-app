package com.ssudio.sfr.payment.presenter;

import com.ssudio.sfr.payment.model.PreferredPaymentModel;
import com.ssudio.sfr.presenter.IEventHandler;

public interface IPaymentPresenter extends IEventHandler {
    void getPayments();
    void getMemberPaymentChannel();
    void savePayment(int channelId, double amount);
}
