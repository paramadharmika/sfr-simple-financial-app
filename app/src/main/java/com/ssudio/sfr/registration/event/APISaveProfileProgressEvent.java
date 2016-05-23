package com.ssudio.sfr.registration.event;

import com.ssudio.sfr.network.event.APICallProgressEvent;

public class APISaveProfileProgressEvent extends APICallProgressEvent {
    public APISaveProfileProgressEvent(boolean isLoading) {
        super(isLoading);
    }
}
