package com.ssudio.sfr.network.ui;

import com.ssudio.sfr.network.event.NetworkConnectivityEvent;

public interface IConnectivityListenerView {
    void showMessage(NetworkConnectivityEvent e);
}
