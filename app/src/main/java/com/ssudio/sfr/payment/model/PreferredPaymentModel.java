package com.ssudio.sfr.payment.model;

import com.google.gson.annotations.SerializedName;
import com.ssudio.sfr.network.request.SFRGeneralPostParameter;

public class PreferredPaymentModel extends SFRGeneralPostParameter {
    private double amount;
    @SerializedName("id_channel_member")
    private int idChannelMember;

    public PreferredPaymentModel(String verificationCode) {
        super(verificationCode);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getIdChannelMember() {
        return idChannelMember;
    }

    public void setIdChannelMember(int idChannelMember) {
        this.idChannelMember = idChannelMember;
    }
}
