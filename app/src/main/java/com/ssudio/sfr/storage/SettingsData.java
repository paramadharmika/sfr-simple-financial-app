package com.ssudio.sfr.storage;

import com.ssudio.sfr.registration.model.UserModel;

import java.io.Serializable;

public class SettingsData implements Serializable {
    private UserModel user;
    /*private String predefinedText;*/

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    /*public String getPredefinedText() {
        return predefinedText;
    }

    public void setPredefinedText(String predefinedText) {
        this.predefinedText = predefinedText;
    }*/
}
