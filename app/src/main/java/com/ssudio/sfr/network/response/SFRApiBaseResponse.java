package com.ssudio.sfr.network.response;

import com.ssudio.sfr.network.request.SFRNetworkRequestType;

public abstract class SFRApiBaseResponse {
    protected SFRNetworkRequestType operation;
    protected String code;
    protected String status;

    public void setOperation(SFRNetworkRequestType operation) {
        this.operation = operation;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public abstract boolean isSuccess();
}
