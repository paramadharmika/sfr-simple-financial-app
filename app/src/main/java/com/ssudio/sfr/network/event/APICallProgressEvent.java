package com.ssudio.sfr.network.event;

public class APICallProgressEvent {
    private boolean isLoading;

    public APICallProgressEvent(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }
}
