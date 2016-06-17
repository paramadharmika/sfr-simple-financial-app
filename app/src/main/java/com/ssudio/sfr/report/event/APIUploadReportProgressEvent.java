package com.ssudio.sfr.report.event;

import com.ssudio.sfr.network.event.APICallProgressEvent;

public class APIUploadReportProgressEvent extends APICallProgressEvent {
    public APIUploadReportProgressEvent(boolean isLoading) {
        super(isLoading);
    }
}
