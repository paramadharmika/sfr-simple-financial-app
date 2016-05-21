package com.ssudio.sfr.registration.presenter;

import com.ssudio.sfr.registration.event.ProfileEvent;

public interface IRegistrationView {
    IRegistrationContainerView getUpperHandler();
    void bindProfile(ProfileEvent e);
    void showRegistrationCallback(ProfileEvent e);
    void showUpdatedRegistrationCallback(ProfileEvent e);
}
