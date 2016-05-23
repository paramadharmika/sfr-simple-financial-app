package com.ssudio.sfr.registration.event;

import com.ssudio.sfr.network.event.APICallProgressEvent;

public class APIGetUserCallProgressEvent extends APICallProgressEvent {
    public APIGetUserCallProgressEvent(boolean isLoading) {
        super(isLoading);
    }
}
