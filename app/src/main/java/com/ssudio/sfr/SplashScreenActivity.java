package com.ssudio.sfr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.components.app.DaggerLocalAuthenticationComponent;
import com.ssudio.sfr.components.app.DaggerLocalStorageComponents;
import com.ssudio.sfr.components.app.DaggerNetworkComponents;
import com.ssudio.sfr.components.app.NetworkComponents;
/*import com.ssudio.sfr.components.ui.DaggerSplashScreenComponent;*/
/*import com.ssudio.sfr.components.ui.SplashScreenComponent;*/
import com.ssudio.sfr.components.ui.BasePresenterComponent;
import com.ssudio.sfr.components.ui.DaggerBasePresenterComponent;
/*import com.ssudio.sfr.components.ui.DaggerSplashScreenComponent;*/
import com.ssudio.sfr.components.ui.SplashScreenComponent;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.NetworkModule;
import com.ssudio.sfr.modules.SplashScreenModule;
import com.ssudio.sfr.splashscreen.model.SplashScreenModel;
import com.ssudio.sfr.splashscreen.presenter.ISplashScreenView;
import com.ssudio.sfr.splashscreen.presenter.SplashScreenPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity implements ISplashScreenView {
    @Inject
    protected LocalAuthenticationService localAuthService;
    @Inject
    protected SplashScreenPresenter splashScreenPresenter;

    private SplashScreenComponent components;

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



        /*DaggerLocalAuthenticationComponent.builder()
                .sFRApplicationModule(((SFRApplication)getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .localAuthenticationModule(new LocalAuthenticationModule())
                .build();*/

        NetworkComponents netComponents = DaggerNetworkComponents.builder()
                /*.sFRApplicationModule(((SFRApplication)getApplication()).getSfrApplicationModule())*/
                .build();


        BasePresenterComponent basePresenterComponent = DaggerBasePresenterComponent.builder()
                .sFRApplicationModule(((SFRApplication)getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .localAuthenticationModule(new LocalAuthenticationModule())
                .networkModule(new NetworkModule())
                .build();

        components = basePresenterComponent.newSplashScreenSubComponent(new SplashScreenModule(this));

        /*components = DaggerSplashScreenComponent.builder()
                .networkComponents(netComponents)
                *//*.localAuthenticationModule(new LocalAuthenticationModule(((SFRApplication)getApplication()).getLocalStorage()))*//*
                .splashScreenModule(new SplashScreenModule(this))
                .build();*/

                /*.sFRApplicationModule(((SFRApplication)getApplication()).getSfrApplicationModule())
                .localAuthenticationModule(new LocalAuthenticationModule())
                .netModule(new NetModule())
                .splashScreenModule(new SplashScreenModule(this))
                .build();*/

        components.inject(this);

        splashScreenPresenter.getAppInfoAsync();
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

        ImageLoader.getInstance().displayImage(model.getAppLogo(), imgSplash,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (localAuthService.isValidUser()) {
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, RegistrationActivity.class));
                        }

                        finish();
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (localAuthService.isValidUser()) {
//                                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
//                                } else {
//                                    startActivity(new Intent(SplashScreenActivity.this, RegistrationActivity.class));
//                                }
//
//                                finish();
//                            }
//                        }, timeToPause);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        splashScreenPresenter.unregisterEventHandler();

        super.onDestroy();
    }
}
