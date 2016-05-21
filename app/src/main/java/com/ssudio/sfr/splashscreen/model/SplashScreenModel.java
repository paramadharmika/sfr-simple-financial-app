package com.ssudio.sfr.splashscreen.model;

import com.google.gson.annotations.SerializedName;

public class SplashScreenModel {
    @SerializedName("id_app")
    private String id;
    @SerializedName("app_name")
    private String appName;
    @SerializedName("app_description")
    private String appDescription;
    @SerializedName("app_logo")
    private String appLogo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }
}
