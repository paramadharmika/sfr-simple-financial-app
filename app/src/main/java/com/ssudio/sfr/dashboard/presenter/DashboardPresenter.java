package com.ssudio.sfr.dashboard.presenter;

import com.ssudio.sfr.dashboard.command.IDashboardCommand;
import com.ssudio.sfr.dashboard.events.DashboardEvent;
import com.ssudio.sfr.dashboard.model.DashboardFeedModel;
import com.ssudio.sfr.dashboard.events.APIDashboardCallProgressEvent;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class DashboardPresenter implements IDashboardPresenter {
    private IDashboardCommand dashboardCommand;
    private IDashboardView view;
    private IConnectivityListenerView connectivityListenerView;
    private ILoadingView loadingView;
    private ArrayList<DashboardFeedModel> temporaryList;

    public DashboardPresenter(IDashboardView view,
                              IConnectivityListenerView connectivityListenerView,
                              ILoadingView loadingView) {
        this.view = view;
        this.connectivityListenerView = connectivityListenerView;
        this.loadingView = loadingView;

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
        loadingView.dismissLoading();
    }

    @Subscribe
    public void onAPIDashboardCallProgressEvent(APIDashboardCallProgressEvent e) {
        loadingView.showLoading();
    }

    @Subscribe
    public void onNetworkEvent(NetworkConnectivityEvent e) {
        connectivityListenerView.showMessage(e);
    }

    @Override
    public void unregisterEventHandler() {
        EventBus.getDefault().unregister(this);
    }
}
