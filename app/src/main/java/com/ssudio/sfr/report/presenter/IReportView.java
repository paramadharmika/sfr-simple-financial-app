package com.ssudio.sfr.report.presenter;

import com.ssudio.sfr.report.model.ReportResponseModel;
import com.ssudio.sfr.ui.IParentChildConnection;

import java.util.ArrayList;

public interface IReportView extends IParentChildConnection {
    void bindReport(ArrayList<ReportResponseModel> result);
    void showReportItemUploaded(boolean isSuccess, String message);
}
