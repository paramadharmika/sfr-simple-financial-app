package com.ssudio.sfr.payment.ui;

public interface IPaymentProfileView extends IPaymentView {
    void showMainActivity();
    void showMessage(boolean isSuccess, String message);
}
