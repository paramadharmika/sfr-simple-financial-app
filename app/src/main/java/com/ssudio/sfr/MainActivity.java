package com.ssudio.sfr;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.components.app.DaggerLocalStorageComponents;
import com.ssudio.sfr.components.app.LocalStorageComponents;
import com.ssudio.sfr.components.ui.BasePresenterComponent;
import com.ssudio.sfr.components.ui.DaggerBasePresenterComponent;
import com.ssudio.sfr.device.QuickstartPreferences;
import com.ssudio.sfr.device.SfrGcmRegistrationIntentService;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.NetworkModule;
import com.ssudio.sfr.ui.IContainerViewCallback;
import com.ssudio.sfr.ui.DashboardFragment;
import com.ssudio.sfr.ui.RegistrationFragment;
import com.ssudio.sfr.ui.ReportFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IContainerViewCallback {
    private static final String FRAGMENT_TAG = "main_container";

    private DashboardFragment dashboardFragment;
    private ReportFragment reportFragment;
    private RegistrationFragment registrationFragment;

    @Inject
    protected SharedPreferences sharedPreferences;
    @Inject
    protected LocalAuthenticationService localAuthenticationService;

    @BindView(R.id.coordinatorLayout)
    protected CoordinatorLayout coordinatorLayout;
    private KProgressHUD loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        BasePresenterComponent basePresenterComponent = DaggerBasePresenterComponent.builder()
                .sFRApplicationModule(((SFRApplication)getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .networkModule(new NetworkModule())
                .build();

        basePresenterComponent.inject(this);

        /*LocalStorageComponents localStorageComponents = DaggerLocalStorageComponents.builder()
                .sFRApplicationModule(((SFRApplication)getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .build();

        localStorageComponents.inject(this);*/

        setupBottomBar();

        changeFragment(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void changeFragment(int fragmentType) {
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setCustomAnimations(R.animator.slide_in_from_right,
                R.animator.slide_out_to_left,
                R.animator.slide_in_from_left,
                R.animator.slide_out_to_right);

        if (fragmentType == 1) {
            if (dashboardFragment == null) {
                dashboardFragment = new DashboardFragment();
            }

            transaction.replace(R.id.mainContainer, dashboardFragment, FRAGMENT_TAG);

            // If this is not the top level media (root), we add it to the fragment back stack,
            // so that actionbar toggle and Back button will work appropriately:
            transaction.commit();
        } else if (fragmentType == 2) {
            if (registrationFragment == null) {
                registrationFragment = new RegistrationFragment();
            }

            transaction.replace(R.id.mainContainer, registrationFragment, FRAGMENT_TAG);

            transaction.commit();
        } else if (fragmentType == 3) {
            if (reportFragment == null) {
                reportFragment = new ReportFragment();
            }

            transaction.replace(R.id.mainContainer, reportFragment, FRAGMENT_TAG);

            transaction.commit();
        }
    }

    private void showShareIntent(String message) {
        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message + " " +
                localAuthenticationService.getLocalAuthenticatedUser().getReffCode());
        sendIntent.setType("text/plain");

        startActivity(sendIntent);
    }

    private void showSettingsActivity() {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);

        startActivity(settingsIntent);
    }

    private void setupBottomBar() {
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.dashboard_title,
                R.drawable.house,
                R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.profile_title,
                R.drawable.avatar,
                R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.report_title,
                R.drawable.book,
                R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.share_title,
                R.drawable.share,
                R.color.colorPrimary);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.settings_title,
                R.drawable.settings,
                R.color.colorPrimary);

        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

        // Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

        // Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setForceTitlesDisplay(true);

        // Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

        // Set current item programmatically
        bottomNavigation.setCurrentItem(0);

        // Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

        // Add or remove notification for each item
        bottomNavigation.setNotification("4", 1);
        bottomNavigation.setNotification("", 1);

        // Set listener
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        changeFragment(position + 1);
                        break;
                    case 3:
                        String message = sharedPreferences.getString("share_text",
                                getString(R.string.pref_default_share_text));

                        showShareIntent(message);
                        break;
                    case 4:
                        showSettingsActivity();
                        break;
                }
            }
        });
    }

    @Override
    public void showMessage(boolean isSuccess, String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingView = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f);

                loadingView.show();
            }
        });
    }

    @Override
    public void dismissLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingView != null) {
                    loadingView.dismiss();
                }
            }
        });
    }
}
