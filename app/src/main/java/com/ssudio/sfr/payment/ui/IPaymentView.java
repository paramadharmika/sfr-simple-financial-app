package com.ssudio.sfr.payment.ui;

import com.ssudio.sfr.payment.model.PaymentModel;
import com.ssudio.sfr.ui.IParentChildConnection;

import java.util.ArrayList;

public interface IPaymentView extends IParentChildConnection {
    void bindPayments(ArrayList<PaymentModel> model);
    void showMessage(boolean isSuccess, String message);
}
