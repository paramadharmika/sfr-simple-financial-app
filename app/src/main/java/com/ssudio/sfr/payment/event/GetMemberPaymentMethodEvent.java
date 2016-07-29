package com.ssudio.sfr.payment.event;

import com.ssudio.sfr.event.BaseActionEvent;
import com.ssudio.sfr.payment.model.MemberPaymentModel;

import java.util.ArrayList;

public class GetMemberPaymentMethodEvent extends BaseActionEvent {
    private ArrayList<MemberPaymentModel> models;

    public GetMemberPaymentMethodEvent(boolean isSuccess, String message, ArrayList<MemberPaymentModel> models) {
        super(isSuccess, message);
        this.models = models;
    }

    public ArrayList<MemberPaymentModel> getModels() {
        return models;
    }
}
