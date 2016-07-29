package com.ssudio.sfr.registration.presenter;

import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.registration.event.APIGetUserCallProgressEvent;
import com.ssudio.sfr.registration.event.APISaveProfileProgressEvent;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.request.SFRGeneralPostParameter;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.registration.command.IGetProfileCommand;
import com.ssudio.sfr.registration.command.IRegistrationCommand;
import com.ssudio.sfr.registration.command.IUpdateProfileCommand;
import com.ssudio.sfr.registration.event.ProfileEvent;
import com.ssudio.sfr.registration.model.UserModel;
import com.ssudio.sfr.storage.SettingsLocalStorage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

public class RegistrationPresenter implements IRegistrationPresenter {
    @Inject
    protected SettingsLocalStorage settingsLocalStorage;
    @Inject
    protected LocalAuthenticationService localAuthenticationService;

    private IRegistrationCommand registrationCommand;
    private IUpdateProfileCommand updateProfileCommand;
    private IGetProfileCommand getProfileCommand;
    private IRegistrationView view;
    private final ILoadingView loadingView;
    private final IConnectivityListenerView connectivityListenerView;

    @Inject
    public RegistrationPresenter(IRegistrationView view,
                                 IConnectivityListenerView connectivityListenerView,
                                 ILoadingView loadingView) {
        this.view = view;
        this.loadingView = loadingView;
        this.connectivityListenerView = connectivityListenerView;

        EventBus.getDefault().register(this);
    }

    public void register(UserModel model) {
        registrationCommand.executeAsync(model);
    }

    @Override
    public void update(UserModel model) {
        updateProfileCommand.executeAsync(model);
    }

    @Override
    public void getUserModel(String verificationCode) {
        getProfileCommand.executeAsync(new SFRGeneralPostParameter(verificationCode));
    }

    @Override
    public void unregisterEventHandler() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onProfileEvent(ProfileEvent e) {
        //always store model to local storage when e.getIsSuccess is true and model is not null
        if (e.getIsSuccess() && e.getModel() != null) {
            localAuthenticationService.storeCredential(e.getModel());
        }

        if (e.getEventType() == ProfileEvent.REGISTERED) {
            view.showRegistrationCallback(e);
        } else if (e.getEventType() == ProfileEvent.UPDATED) {
            view.showUpdatedRegistrationCallback(e);
        } else if (e.getEventType() == ProfileEvent.LOADED) {
            view.bindProfile(e);
        }
    }

    @Subscribe
    public void onAPIGetUserCallProgressEvent(APIGetUserCallProgressEvent e) {
        if (e.isLoading()) {
            loadingView.showLoading();
        } else {
            loadingView.dismissLoading();
        }
    }

    @Subscribe
    public void onAPISaveProfileProgressEvent(APISaveProfileProgressEvent e) {
        if (e.isLoading()) {
            loadingView.showLoading();
        } else {
            loadingView.dismissLoading();
        }
    }

    @Subscribe
    public void onNetworkEvent(NetworkConnectivityEvent e) {
        connectivityListenerView.showMessage(e);
    }

    public void setRegistrationCommand(IRegistrationCommand registrationCommand) {
        this.registrationCommand = registrationCommand;
    }

    public void setUpdateProfileCommand(IUpdateProfileCommand updateProfileCommand) {
        this.updateProfileCommand = updateProfileCommand;
    }

    public void setGetProfileCommand(IGetProfileCommand getProfileCommand) {
        this.getProfileCommand = getProfileCommand;
    }
}
