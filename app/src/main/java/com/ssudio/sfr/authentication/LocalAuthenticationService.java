package com.ssudio.sfr.authentication;

import com.ssudio.sfr.registration.model.UserModel;
import com.ssudio.sfr.storage.SettingsData;
import com.ssudio.sfr.storage.SettingsLocalStorage;

public class LocalAuthenticationService {
    private SettingsLocalStorage settingsLocalStorage;

    public LocalAuthenticationService(SettingsLocalStorage settingsLocalStorage) {
        this.settingsLocalStorage = settingsLocalStorage;
    }

    public boolean isValidUser() {
        SettingsData settingsData = settingsLocalStorage.getSettingsData();

        if (settingsData == null) {
            return false;
        }

        UserModel user = settingsData.getUser();

        if (user == null) {
            return false;
        }

        return !user.getVerificationCode().isEmpty();
    }

    public UserModel getLocalAuthenticatedUser() {
        SettingsData settingsData = settingsLocalStorage.getSettingsData();

        if (settingsData == null) {
            return new UserModel("", "", "");
        }

        UserModel user = settingsData.getUser();

        if (user == null) {
            return new UserModel("", "", "");
        }

        return user;
    }

    public String getLocalVerificationCode() {
        SettingsData settingsData = settingsLocalStorage.getSettingsData();

        if (settingsData == null) {
            return "";
        }

        UserModel user = settingsData.getUser();

        if (user == null) {
            return "";
        }

        return user.getVerificationCode();
    }

    public void storeCredential(UserModel model) {
        SettingsData data = settingsLocalStorage.getSettingsData();

        data.setUser(model);

        settingsLocalStorage.saveSettingsData(data);
    }
}
