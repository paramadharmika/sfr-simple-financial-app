package com.ssudio.sfr.payment.event;

import com.ssudio.sfr.network.event.APICallProgressEvent;

public class APIPaymentProgressEvent extends APICallProgressEvent {
    public APIPaymentProgressEvent(boolean isLoading) {
        super(isLoading);
    }
}
