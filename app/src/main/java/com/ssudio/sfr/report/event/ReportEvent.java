package com.ssudio.sfr.report.event;

import com.ssudio.sfr.report.model.ReportResponseModel;

import java.util.ArrayList;

public class ReportEvent {
    public static int FAILED_TO_GENERATE_REPORT = 0;
    public static int REPORT_GENERATED = 1;

    private int eventStatus;
    private ArrayList<ReportResponseModel> models;

    public ReportEvent(int eventStatus, ArrayList<ReportResponseModel> models) {
        this.eventStatus = eventStatus;
        this.models = models;
    }

    public int getEventStatus() {
        return eventStatus;
    }

    public ArrayList<ReportResponseModel> getReports() {
        return models;
    }
}
