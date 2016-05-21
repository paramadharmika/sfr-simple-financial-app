package com.ssudio.sfr.report.model;

import com.google.gson.annotations.SerializedName;
import com.ssudio.sfr.network.request.SFRGeneralPostParameter;

public class ReportRequestModel extends SFRGeneralPostParameter {
    @SerializedName("date_start")
    private String startDate;
    @SerializedName("date_end")
    private String endDate;

    public ReportRequestModel(String startDate, String endDate, String verificationCode) {
        super(verificationCode);

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
