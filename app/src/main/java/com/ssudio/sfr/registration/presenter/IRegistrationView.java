package com.ssudio.sfr.registration.presenter;

import com.ssudio.sfr.registration.event.ProfileEvent;
import com.ssudio.sfr.ui.IParentChildConnection;

public interface IRegistrationView extends IParentChildConnection {
    void bindProfile(ProfileEvent e);
    void showRegistrationCallback(ProfileEvent e);
    void showUpdatedRegistrationCallback(ProfileEvent e);
}
