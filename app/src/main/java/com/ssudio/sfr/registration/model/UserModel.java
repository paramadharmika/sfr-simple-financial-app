package com.ssudio.sfr.registration.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {
    @SerializedName("nama")
    private String name;
    @SerializedName("no_hp")
    private String phoneNumber;
    @SerializedName("verification_code")
    private final String verificationCode;

    @SerializedName("id_member")
    private int id;
    @SerializedName("license_until")
    private String licenseUntil;
    @SerializedName("status")
    private String status;
    @SerializedName("level")
    private String level;
    @SerializedName("join_date")
    private String joinDate;

    public UserModel(String name, String phoneNumber, String verificationCode) {
        this.id = 0;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLicenseUntil() {
        return licenseUntil;
    }

    public void setLicenseUntil(String licenseUntil) {
        this.licenseUntil = licenseUntil;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
}
