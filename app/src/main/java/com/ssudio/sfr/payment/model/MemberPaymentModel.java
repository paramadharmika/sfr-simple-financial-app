package com.ssudio.sfr.payment.model;

import com.google.gson.annotations.SerializedName;

public class MemberPaymentModel {
    @SerializedName("id_channel")
    private int idChannel;
    @SerializedName("channel_name")
    private String channelName;
    @SerializedName("id_channel_member")
    private String idChannelMember;
    @SerializedName("payment_to")
    private String paymentTo;
    @SerializedName("payment_name")
    private String paymentName;

    public int getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getIdChannelMember() {
        return idChannelMember;
    }

    public void setIdChannelMember(String idChannelMember) {
        this.idChannelMember = idChannelMember;
    }

    public String getPaymentTo() {
        return paymentTo;
    }

    public void setPaymentTo(String paymentTo) {
        this.paymentTo = paymentTo;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
