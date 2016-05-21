package com.ssudio.sfr.network.response;

import com.ssudio.sfr.network.request.SFRNetworkRequestType;

public class SFRApiPostResponse extends SFRApiBaseResponse {
    private SFRApiExtendedInfo data;

    @Override
    public boolean isSuccess() {
        if (code != null) {
            if (operation == SFRNetworkRequestType.Insert) {
                return code.equals("00") && data.getAffectedRows() > 0 && data.getInsertId() > 0;
            } else if (operation == SFRNetworkRequestType.Update) {
                return code.equals("00") && data.getAffectedRows() > 0;
            } else {
                return code.equals("00") && data.getAffectedRows() == 0;
            }
        }

        return false;
    }

    public SFRApiExtendedInfo getData() {
        return data;
    }

    public void setData(SFRApiExtendedInfo data) {
        this.data = data;
    }
}
