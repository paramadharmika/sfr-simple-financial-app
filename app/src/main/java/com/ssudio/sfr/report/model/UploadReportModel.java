package com.ssudio.sfr.report.model;

import com.ssudio.sfr.network.request.SFRGeneralPostParameter;

public class UploadReportModel extends SFRGeneralPostParameter {
    private final String fileName;
    private final int id;

    public UploadReportModel(String verificationCode, String fileName, int id) {
        super(verificationCode);
        this.fileName = fileName;
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public int getId() {
        return id;
    }
}
