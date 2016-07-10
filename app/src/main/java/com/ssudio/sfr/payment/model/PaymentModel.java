package com.ssudio.sfr.payment.model;

import com.google.gson.annotations.SerializedName;

public class PaymentModel {
    @SerializedName("id_channel")
    private int idChannel;
    @SerializedName("channel_name")
    private String channelName;
    @SerializedName("channel_info")
    private String channelInfo;
    @SerializedName("min_payment")
    private String minPayment;

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

    public String getChannelInfo() {
        return channelInfo;
    }

    public void setChannelInfo(String channelInfo) {
        this.channelInfo = channelInfo;
    }

    public String getMinPayment() {
        return minPayment;
    }

    public void setMinPayment(String minPayment) {
        this.minPayment = minPayment;
    }
}
