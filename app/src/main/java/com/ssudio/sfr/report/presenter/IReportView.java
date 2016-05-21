package com.ssudio.sfr.report.presenter;

import com.ssudio.sfr.report.model.ReportResponseModel;

import java.util.ArrayList;

public interface IReportView {
    void bindReport(ArrayList<ReportResponseModel> result);
}
