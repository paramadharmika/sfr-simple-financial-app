package com.ssudio.sfr.modules;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.dashboard.command.DashboardCommand;
import com.ssudio.sfr.dashboard.command.IDashboardCommand;
import com.ssudio.sfr.dashboard.presenter.DashboardPresenter;
import com.ssudio.sfr.dashboard.presenter.IDashboardPresenter;
import com.ssudio.sfr.dashboard.presenter.IDashboardView;
import com.ssudio.sfr.scope.UserScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class DashboardModule {
    private IDashboardView view;

    public DashboardModule(IDashboardView view) {
        this.view = view;
    }

    @UserScope
    @Provides
    public IDashboardPresenter getPresenter(IDashboardView view, IDashboardCommand dashboardCommand) {
        DashboardPresenter presenter = new DashboardPresenter(view);

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
    public IDashboardCommand getDashboardCommand(OkHttpClient client, Gson gson,
                                                 IAppConfiguration appConfiguration) {
        return new DashboardCommand(client, gson, appConfiguration);
    }
}
