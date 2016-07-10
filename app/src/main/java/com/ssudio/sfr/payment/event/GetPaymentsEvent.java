package com.ssudio.sfr.payment.event;

import com.ssudio.sfr.event.BaseActionEvent;
import com.ssudio.sfr.payment.model.PaymentModel;

import java.util.ArrayList;

public class GetPaymentsEvent extends BaseActionEvent {
    private ArrayList<PaymentModel> payments;

    public GetPaymentsEvent(boolean isSuccess, String message, ArrayList<PaymentModel> payments) {
        super(isSuccess, message);

        this.payments = payments;
    }

    public ArrayList<PaymentModel> getPayments() {
        return payments;
    }
}
