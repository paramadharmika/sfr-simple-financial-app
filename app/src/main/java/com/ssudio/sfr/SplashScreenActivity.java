package com.ssudio.sfr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.components.ui.BasePresenterComponent;
import com.ssudio.sfr.components.ui.DaggerBasePresenterComponent;
import com.ssudio.sfr.components.ui.SplashScreenComponent;
import com.ssudio.sfr.device.QuickstartPreferences;
import com.ssudio.sfr.device.SfrGcmRegistrationIntentService;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.NetworkModule;
import com.ssudio.sfr.modules.SplashScreenModule;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.splashscreen.model.SplashScreenModel;
import com.ssudio.sfr.splashscreen.presenter.ISplashScreenView;
import com.ssudio.sfr.splashscreen.presenter.SplashScreenPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity
        implements ISplashScreenView, IConnectivityListenerView {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashScreenActivity";

    @Inject
    protected LocalAuthenticationService localAuthService;
    @Inject
    protected SharedPreferences sharedPreferences;
    @Inject
    protected SplashScreenPresenter splashScreenPresenter;

    private SplashScreenComponent components;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private boolean isReceiverRegistered;

    @BindView(R.id.coordinatorLayout)
    protected CoordinatorLayout coordinatorLayout;
    @BindView(R.id.txtAppName)
    protected TextView txtAppName;
    @BindView(R.id.txtAppDescription)
    protected TextView txtAppDescription;
    @BindView(R.id.imgSplash)
    protected ImageView imgSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);

        BasePresenterComponent basePresenterComponent = DaggerBasePresenterComponent.builder()
                .sFRApplicationModule(((SFRApplication)getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .localAuthenticationModule(new LocalAuthenticationModule())
                .networkModule(new NetworkModule())
                .build();

        components = basePresenterComponent.newSplashScreenSubComponent(new SplashScreenModule(this, this));

        components.inject(this);

        getDeviceRegistrationId();
    }

    private void getDeviceRegistrationId() {
        //below is the google cloud messaging registration process
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                /*SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);*/

                splashScreenPresenter.getAppInfoAsync();

                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

                if (sentToken) {
                    Toast.makeText(context, "token send", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "token error", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, SfrGcmRegistrationIntentService.class);

            startService(intent);
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this)
                    .registerReceiver(mRegistrationBroadcastReceiver,
                            new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void bindSplashScreenInfo(final SplashScreenModel model) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bindInfo(model);
            }
        });
    }

    private void bindInfo(SplashScreenModel model) {
        txtAppName.setText(model.getAppName());
        txtAppDescription.setText(model.getAppDescription());

        final int delayTime = 2000;

        ImageLoader.getInstance().displayImage(model.getAppLogo(), imgSplash,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (localAuthService.isValidUser()) {
                                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                } else {
                                    startActivity(new Intent(SplashScreenActivity.this, RegistrationActivity.class));
                                }

                                finish();
                            }
                        }, delayTime);
                    }
                });
    }

    @Override
    public void showMessage(final NetworkConnectivityEvent e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message;

                if (e.isConnected()) {
                    message = getString(R.string.message_network_connected);
                } else {
                    message = getString(R.string.message_network_is_not_connected);

                    showNetworkNotConnected(message);
                }
            }
        });
    }

    private void showNetworkNotConnected(String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

        textView.setTextColor(Color.YELLOW);

        snackbar.show();

        /*((android.support.design.widget.CoordinatorLayout.LayoutParams) snackbar.getView().getLayoutParams()).setBehavior(null);*/

    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver();
    }

    @Override
    protected void onDestroy() {
        splashScreenPresenter.unregisterEventHandler();

        super.onDestroy();
    }
}
