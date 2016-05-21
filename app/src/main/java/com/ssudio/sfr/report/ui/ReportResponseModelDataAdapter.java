package com.ssudio.sfr.report.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.ssudio.sfr.report.model.ReportResponseModel;

public class ReportResponseModelDataAdapter extends ArrayAdapter<ReportResponseModel> {
    public ReportResponseModelDataAdapter(Context context, int resource, ReportResponseModel[] objects) {
        super(context, resource, objects);
    }
}
