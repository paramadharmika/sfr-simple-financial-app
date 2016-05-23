package com.ssudio.sfr.report.event;

import com.ssudio.sfr.network.event.APICallProgressEvent;

public class APIGetReportProgressEvent extends APICallProgressEvent {
    public APIGetReportProgressEvent(boolean isLoading) {
        super(isLoading);
    }
}
