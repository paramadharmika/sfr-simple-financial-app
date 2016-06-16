package com.ssudio.sfr.report.presenter;

import com.ssudio.sfr.presenter.IEventHandler;
import com.ssudio.sfr.report.model.ReportRequestModel;
import com.ssudio.sfr.report.model.UploadReportModel;

public interface IReportPresenter extends IEventHandler {
    void getReport(ReportRequestModel model);
    void uploadFile(UploadReportModel model);
}
