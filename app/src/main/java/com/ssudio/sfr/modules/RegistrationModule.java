package com.ssudio.sfr.modules;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.registration.command.GetProfileCommand;
import com.ssudio.sfr.registration.command.IGetProfileCommand;
import com.ssudio.sfr.registration.command.IRegistrationCommand;
import com.ssudio.sfr.registration.command.IUpdateProfileCommand;
import com.ssudio.sfr.registration.command.RegistrationCommand;
import com.ssudio.sfr.registration.command.UpdateProfileCommand;
import com.ssudio.sfr.registration.model.UserModel;
import com.ssudio.sfr.registration.presenter.IRegistrationView;
import com.ssudio.sfr.registration.presenter.RegistrationPresenter;
import com.ssudio.sfr.scope.UserScope;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class RegistrationModule {
    private IRegistrationView view;

    public RegistrationModule(IRegistrationView view) {
        this.view = view;
    }

    @Provides
    @UserScope
    public RegistrationPresenter getPresenter(IRegistrationView view,
                                              IRegistrationCommand registrationCommand,
                                              IUpdateProfileCommand updateProfileCommand,
                                              IGetProfileCommand getProfileCommand
                                              ) {
        RegistrationPresenter presenter = new RegistrationPresenter(view);

        presenter.setRegistrationCommand(registrationCommand);
        presenter.setUpdateProfileCommand(updateProfileCommand);
        presenter.setGetProfileCommand(getProfileCommand);

        return presenter;
    }

    @Provides
    @UserScope
    public IRegistrationView getRegistrationView() {
        return this.view;
    }

    @Provides
    @UserScope
    public IRegistrationCommand getRegistrationCommand(OkHttpClient client, Gson gson,
                                                       IAppConfiguration appConfiguration) {
        return new RegistrationCommand(client, gson, appConfiguration);
    }

    @Provides
    @UserScope
    public IUpdateProfileCommand getUpdateProfileCommand(OkHttpClient client, Gson gson,
                                                         IAppConfiguration appConfiguration) {
        return new UpdateProfileCommand(client, gson, appConfiguration);
    }

    @Provides
    @UserScope
    public IGetProfileCommand getProfileCommand(OkHttpClient client, Gson gson,
                                                     IAppConfiguration appConfiguration) {
        return new GetProfileCommand(client, gson, appConfiguration);
    }
}
