package com.ssudio.sfr.network.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ssudio.sfr.network.event.NetworkConnectivityEvent;

import org.greenrobot.eventbus.EventBus;

public class NetworkStateReceiver extends BroadcastReceiver {
    public NetworkStateReceiver() { }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);

            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                Log.i("app", "Network " + ni.getTypeName() + " connected");

                EventBus.getDefault().post(new NetworkConnectivityEvent(true));
            } else {
                EventBus.getDefault().post(new NetworkConnectivityEvent(false));
            }
        }

        if (intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            //todo: should post network connected (NetworkEvent)
            Log.d("app","There's no network connectivity");

            EventBus.getDefault().post(new NetworkConnectivityEvent(false));
        }
    }
}
