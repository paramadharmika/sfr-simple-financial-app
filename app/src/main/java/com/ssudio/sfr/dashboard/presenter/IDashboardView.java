package com.ssudio.sfr.dashboard.presenter;

import com.ssudio.sfr.dashboard.model.DashboardFeedModel;

import java.util.ArrayList;

public interface IDashboardView {
    void bindContent(ArrayList<DashboardFeedModel> result);
}
