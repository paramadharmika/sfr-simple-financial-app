package com.ssudio.sfr.dashboard.presenter;

import com.ssudio.sfr.dashboard.command.IDashboardCommand;
import com.ssudio.sfr.dashboard.events.DashboardEvent;
import com.ssudio.sfr.dashboard.model.DashboardFeedModel;
import com.ssudio.sfr.registration.event.ProfileEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

public class DashboardPresenter implements IDashboardPresenter {
    private IDashboardCommand dashboardCommand;
    private IDashboardView view;
    private ArrayList<DashboardFeedModel> temporaryList;

    public DashboardPresenter(IDashboardView view) {
        this.view = view;

        EventBus.getDefault().register(this);
    }

    public void setDashboardCommand(IDashboardCommand dashboardCommand) {
        this.dashboardCommand = dashboardCommand;
    }

    @Override
    public void getDashboardContent() {
        dashboardCommand.executeAsync("");
    }

    @Subscribe
    public void onDashboardEvent(DashboardEvent e) {
        /*if (e.getEventType() == DashboardEvent.INITIAL_LOAD) {
            temporaryList = e.getModel();
        } else if (e.getEventType() == DashboardEvent.PULL_TO_REFRESH) {
            temporaryList.addAll(0, e.getModel());
        }*/

        view.bindContent(e.getModel());
    }

    @Override
    public void unregisterEventHandler() {
        EventBus.getDefault().unregister(this);
    }
}
