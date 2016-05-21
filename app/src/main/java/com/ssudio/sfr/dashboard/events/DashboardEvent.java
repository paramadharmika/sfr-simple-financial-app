package com.ssudio.sfr.dashboard.events;

import com.ssudio.sfr.dashboard.model.DashboardFeedModel;

import java.util.ArrayList;

public class DashboardEvent {
    public static int PULL_TO_REFRESH = 0;
    public static int INITIAL_LOAD = 1;

    private int eventType;
    private ArrayList<DashboardFeedModel> models;

    public DashboardEvent(int eventType, ArrayList<DashboardFeedModel> models) {
        this.eventType = eventType;
        this.models = models;
    }

    public ArrayList<DashboardFeedModel> getModel() {
        return models;
    }

    public int getEventType() {
        return eventType;
    }
}
