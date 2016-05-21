package com.ssudio.sfr.network.request;

import com.google.gson.annotations.SerializedName;

public class SFRGeneralPostParameter  {
    @SerializedName("verification_code")
    private final String verificationCode;

    public SFRGeneralPostParameter(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}
