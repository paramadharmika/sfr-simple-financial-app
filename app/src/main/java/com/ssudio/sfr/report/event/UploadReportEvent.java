package com.ssudio.sfr.report.event;

public class UploadReportEvent {
    public static int FAILED_TO_UPLOAD_REPORT = 0;
    public static int REPORT_UPLOADED = 1;

    private int eventStatus;
    private String message;

    public UploadReportEvent(int eventStatus, String message) {
        this.eventStatus = eventStatus;
        this.message = message;
    }

    public int getEventStatus() {
        return eventStatus;
    }

    public String getMessage() {
        return message;
    }
}
