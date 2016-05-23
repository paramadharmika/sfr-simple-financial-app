package com.ssudio.sfr.dashboard.events;

import com.ssudio.sfr.network.event.APICallProgressEvent;

public class APIDashboardCallProgressEvent extends APICallProgressEvent {
    public APIDashboardCallProgressEvent(boolean isLoading) {
        super(isLoading);
    }
}
