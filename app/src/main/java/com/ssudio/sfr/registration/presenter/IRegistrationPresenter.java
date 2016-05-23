package com.ssudio.sfr.registration.presenter;

import com.ssudio.sfr.presenter.IEventHandler;
import com.ssudio.sfr.registration.model.UserModel;

public interface IRegistrationPresenter extends IEventHandler {
    void register(UserModel model);
    void update(UserModel model);
    void getUserModel(String verificationCode);
}
