package com.ssudio.sfr.dashboard.presenter;

import com.ssudio.sfr.dashboard.model.DashboardFeedModel;
import com.ssudio.sfr.ui.IParentChildConnection;

import java.util.ArrayList;

public interface IDashboardView extends IParentChildConnection {
    void bindContent(ArrayList<DashboardFeedModel> result);
}
