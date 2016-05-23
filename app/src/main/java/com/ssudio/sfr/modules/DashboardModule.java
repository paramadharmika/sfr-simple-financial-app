package com.ssudio.sfr.modules;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.dashboard.command.DashboardCommand;
import com.ssudio.sfr.dashboard.command.IDashboardCommand;
import com.ssudio.sfr.dashboard.presenter.DashboardPresenter;
import com.ssudio.sfr.dashboard.presenter.IDashboardPresenter;
import com.ssudio.sfr.dashboard.presenter.IDashboardView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.scope.UserScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class DashboardModule {
    private IDashboardView view;
    private IConnectivityListenerView networkListenerView;
    private ILoadingView loadingView;

    public DashboardModule(IDashboardView view,
                           IConnectivityListenerView networkListenerView,
                           ILoadingView loadingView) {
        this.view = view;
        this.networkListenerView = networkListenerView;
        this.loadingView = loadingView;
    }

    @UserScope
    @Provides
    public IDashboardPresenter getPresenter(IDashboardView view,
                                            IConnectivityListenerView networkListenerView,
                                            ILoadingView loadingView,
                                            IDashboardCommand dashboardCommand) {
        DashboardPresenter presenter = new DashboardPresenter(view, networkListenerView, loadingView);

        presenter.setDashboardCommand(dashboardCommand);

        return presenter;
    }

    @UserScope
    @Provides
    public IDashboardView getDashboardView() {
        return this.view;
    }

    @UserScope
    @Provides
    public IConnectivityListenerView getNetworkListenerView() {
        return this.networkListenerView;
    }

    @UserScope
    @Provides
    public ILoadingView getLoadingView() {
        return this.loadingView;
    }

    @UserScope
    @Provides
    public IDashboardCommand getDashboardCommand(OkHttpClient client, Gson gson,
                                                 IAppConfiguration appConfiguration) {
        return new DashboardCommand(client, gson, appConfiguration);
    }
}
