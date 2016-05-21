package com.ssudio.sfr.network.response;

import java.util.ArrayList;

public class SFRApiGetResponse<T> extends SFRApiBaseResponse {
    private ArrayList<T> data;

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return code.equals("00");
    }
}
