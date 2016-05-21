package com.ssudio.sfr.report.presenter;

import com.ssudio.sfr.presenter.IEventHandler;
import com.ssudio.sfr.report.model.ReportRequestModel;

public interface IReportPresenter extends IEventHandler {
    void getReport(ReportRequestModel model);
}
