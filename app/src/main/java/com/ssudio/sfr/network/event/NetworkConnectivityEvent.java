package com.ssudio.sfr.network.event;

public class NetworkConnectivityEvent {
    private boolean isConnected;

    public NetworkConnectivityEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
