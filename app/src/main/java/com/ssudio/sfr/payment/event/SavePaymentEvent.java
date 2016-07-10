package com.ssudio.sfr.payment.event;

import com.ssudio.sfr.event.BaseActionEvent;

public class SavePaymentEvent extends BaseActionEvent {
    public SavePaymentEvent(boolean isSuccess, String message) {
        super(isSuccess, message);
    }
}
