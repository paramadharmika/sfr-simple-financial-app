package com.ssudio.sfr.ui;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ssudio.sfr.MainActivity;
import com.ssudio.sfr.R;
import com.ssudio.sfr.SFRApplication;
import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.components.app.DaggerLocalStorageComponents;
import com.ssudio.sfr.components.app.DaggerNetworkComponents;
import com.ssudio.sfr.components.app.NetworkComponents;
import com.ssudio.sfr.components.ui.BasePresenterComponent;
import com.ssudio.sfr.components.ui.DaggerBasePresenterComponent;
import com.ssudio.sfr.components.app.LocalStorageComponents;
import com.ssudio.sfr.components.ui.RegistrationComponents;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.NetworkModule;
import com.ssudio.sfr.modules.RegistrationModule;
import com.ssudio.sfr.registration.event.ProfileEvent;
import com.ssudio.sfr.registration.model.UserModel;
import com.ssudio.sfr.registration.presenter.IRegistrationContainerView;
import com.ssudio.sfr.registration.presenter.IRegistrationView;
import com.ssudio.sfr.registration.presenter.RegistrationPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment extends Fragment implements IRegistrationView {
    @Inject
    LocalAuthenticationService localAuthService;
    @Inject
    RegistrationPresenter registrationPresenter;

    private RegistrationComponents components;

    @BindView(R.id.btnSubmit)
    protected Button btnSubmit;
    @BindView(R.id.txtName)
    protected EditText txtName;
    @BindView(R.id.txtPhone)
    protected EditText txtPhone;
    @BindView(R.id.txtVerificationCode)
    protected EditText txtVerificationCode;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        LocalStorageComponents localStorageComponents = DaggerLocalStorageComponents.builder()
                .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .build();

        NetworkComponents netComponents = DaggerNetworkComponents.builder()
                /*.sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())*/
                .build();

        /*components = DaggerRegistrationComponents.builder()
                .localAuthenticationModule(new LocalAuthenticationModule(((SFRApplication)getActivity().getApplication()).getLocalStorage()))
                .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                .netModule(new NetModule())
                .registrationModule(new RegistrationModule(this))
                .build();*/

        /*components = DaggerRegistrationComponents.builder()
                *//*.localAuthenticationModule(new LocalAuthenticationModule(((SFRApplication)getActivity().getApplication()).getLocalStorage()))*//*
                .networkComponents(netComponents)
                .registrationModule(new RegistrationModule(this))
                .build();*/

        BasePresenterComponent basePresenterComponent = DaggerBasePresenterComponent.builder()
                .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .localAuthenticationModule(new LocalAuthenticationModule())
                .networkModule(new NetworkModule())
                .build();

        components = basePresenterComponent.newRegistrationSubComponent(new RegistrationModule(this));

        components.inject(this);

        basePresenterComponent.inject(registrationPresenter);

        /*localStorageComponents.inject(registrationPresenter);*/

        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        ButterKnife.bind(this, v);

        setupViews();

        return v;
    }

    private void setupViews() {
        if (localAuthService.isValidUser()) {
            registrationPresenter.getUserModelAsync(localAuthService.getLocalVerificationCode());
        }
    }

    @OnClick(R.id.btnSubmit)
    public void btnSubmit_Click() {
        UserModel model;

        if (localAuthService.isValidUser()) {
            model = localAuthService.getLocalAuthenticatedUser();

            model.setName(txtName.getText().toString());
            model.setPhoneNumber(txtPhone.getText().toString());

            registrationPresenter.update(model);
        } else {
            model = new UserModel(txtName.getText().toString(),
                    txtPhone.getText().toString(),
                    txtVerificationCode.getText().toString());

            registrationPresenter.register(model);
        }
    }

    @Override
    public IRegistrationContainerView getUpperHandler() {
        return (IRegistrationContainerView) getActivity();
    }

    @Override
    public void bindProfile(final ProfileEvent e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bindInfo(e);
            }
        });
    }

    private void bindInfo(ProfileEvent e) {
        if (e.getIsSuccess() && e.getModel() != null) {
            txtName.setText(e.getModel().getName());
            txtPhone.setText(e.getModel().getPhoneNumber());
            txtVerificationCode.setText(e.getModel().getVerificationCode());
        } else {
            getUpperHandler().showMessage(e.getIsSuccess(), e.getMessage());
        }
    }

    @Override
    public void showRegistrationCallback(ProfileEvent e) {
        if (e.getIsSuccess()) {
            Intent intent = new Intent(getActivity(), MainActivity.class);

            startActivity(intent);

            getActivity().finish();
        } else {
            getUpperHandler().showMessage(e.getIsSuccess(), e.getMessage());
        }
    }

    @Override
    public void showUpdatedRegistrationCallback(ProfileEvent e) {
        getUpperHandler().showMessage(e.getIsSuccess(), e.getMessage());
    }

    @Override
    public void onDestroy() {
        registrationPresenter.unregisterEventHandler();

        super.onDestroy();
    }
}
