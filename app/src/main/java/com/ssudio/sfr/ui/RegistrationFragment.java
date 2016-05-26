package com.ssudio.sfr.ui;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.ssudio.sfr.MainActivity;
import com.ssudio.sfr.R;
import com.ssudio.sfr.SFRApplication;
import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.components.ui.BasePresenterComponent;
import com.ssudio.sfr.components.ui.DaggerBasePresenterComponent;
import com.ssudio.sfr.components.ui.RegistrationComponents;
import com.ssudio.sfr.device.QuickstartPreferences;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.NetworkModule;
import com.ssudio.sfr.modules.RegistrationModule;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.registration.event.ProfileEvent;
import com.ssudio.sfr.registration.model.UserModel;
import com.ssudio.sfr.registration.presenter.IRegistrationView;
import com.ssudio.sfr.registration.presenter.RegistrationPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment extends Fragment
        implements IRegistrationView, IConnectivityListenerView, ILoadingView, Validator.ValidationListener {
    @Inject
    LocalAuthenticationService localAuthService;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    RegistrationPresenter registrationPresenter;

    private RegistrationComponents components;

    @BindView(R.id.btnSubmit)
    protected Button btnSubmit;
    @Required(order = 1)
    @BindView(R.id.txtName)
    protected EditText txtName;
    @Required(order = 2)
    @BindView(R.id.txtPhone)
    protected EditText txtPhone;
    @Required(order = 3)
    @BindView(R.id.txtVerificationCode)
    protected EditText txtVerificationCode;
    private Validator userDetailsValidator;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        BasePresenterComponent basePresenterComponent = DaggerBasePresenterComponent.builder()
                .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .localAuthenticationModule(new LocalAuthenticationModule())
                .networkModule(new NetworkModule())
                .build();

        components = basePresenterComponent.newRegistrationSubComponent(new RegistrationModule(this, this, this));

        components.inject(this);

        basePresenterComponent.inject(registrationPresenter);

        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        ButterKnife.bind(this, v);

        setupViews();

        setupValidators();

        return v;
    }

    private void setupViews() {
        if (localAuthService.isValidUser()) {
            registrationPresenter.getUserModel(localAuthService.getLocalVerificationCode());
        }
    }

    private void setupValidators() {
        userDetailsValidator = new Validator(this);

        //'this' class implements ValidationListener
        userDetailsValidator.setValidationListener(this);
    }

    @OnClick(R.id.btnSubmit)
    public void btnSubmit_Click() {
        userDetailsValidator.validate();
    }

    @Override
    public IContainerViewCallback getParentView() {
        return (IContainerViewCallback) getActivity();
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
            txtVerificationCode.setEnabled(false);
        } else {
            getParentView().showMessage(e.getIsSuccess(), e.getMessage());
        }

        dismissLoading();
    }

    @Override
    public void showRegistrationCallback(ProfileEvent e) {
        if (e.getIsSuccess()) {
            Intent intent = new Intent(getActivity(), MainActivity.class);

            startActivity(intent);

            getActivity().finish();
        } else {
            getParentView().showMessage(e.getIsSuccess(), e.getMessage());

            dismissLoading();
        }
    }

    @Override
    public void showUpdatedRegistrationCallback(ProfileEvent e) {
        getParentView().showMessage(e.getIsSuccess(), e.getMessage());

        dismissLoading();
    }

    @Override
    public void showMessage(final NetworkConnectivityEvent e) {
        final Activity activity = getActivity();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message;

                if (e.isConnected()) {
                    message = getString(R.string.message_network_connected);
                } else {
                    message = getString(R.string.message_network_is_not_connected);
                }

                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showLoading() {
        getParentView().showLoading();
    }

    @Override
    public void dismissLoading() {
        getParentView().dismissLoading();
    }


    @Override
    public void onValidationSucceeded() {
        clearErrorMessageOnEditText();

        UserModel model;

        if (localAuthService.isValidUser()) {
            model = localAuthService.getLocalAuthenticatedUser();

            model.setName(txtName.getText().toString());
            model.setPhoneNumber(txtPhone.getText().toString());

            registrationPresenter.update(model);
        } else {
            model = new UserModel(txtName.getText().toString(),
                    txtPhone.getText().toString(),
                    txtVerificationCode.getText().toString(),
                    sharedPreferences.getString(QuickstartPreferences.DEVICE_REGISTRATION_TOKEN, ""));

            registrationPresenter.register(model);
        }
    }

    private void clearErrorMessageOnEditText() {
        txtName.setError(null);
        txtPhone.setError(null);
        txtVerificationCode.setError(null);
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String failureMessage = failedRule.getFailureMessage();

        failedView.requestFocus();

        if (failedView instanceof EditText) {
            ((EditText)failedView).setError(failureMessage);
        }
    }

    @Override
    public void onDestroy() {
        registrationPresenter.unregisterEventHandler();

        super.onDestroy();
    }
}
