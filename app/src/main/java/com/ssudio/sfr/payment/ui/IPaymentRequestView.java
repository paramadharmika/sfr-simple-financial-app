package com.ssudio.sfr.payment.ui;

public interface IPaymentRequestView extends IPaymentView {
    void showMessage(boolean isSuccess, String message);
}
