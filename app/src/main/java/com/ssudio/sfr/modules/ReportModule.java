package com.ssudio.sfr.modules;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.report.command.IReportCommand;
import com.ssudio.sfr.report.command.ReportCommand;
import com.ssudio.sfr.report.presenter.IReportView;
import com.ssudio.sfr.report.presenter.ReportPresenter;
import com.ssudio.sfr.scope.UserScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class ReportModule {
    private IReportView view;
    private final IConnectivityListenerView connectivityListenerView;
    private final ILoadingView loadingView;

    public ReportModule(IReportView view, IConnectivityListenerView connectivityListenerView, ILoadingView loadingView) {
        this.view = view;
        this.connectivityListenerView = connectivityListenerView;
        this.loadingView = loadingView;
    }

    @UserScope
    @Provides
    public ReportPresenter getPresenter(IReportView view,
                                        IConnectivityListenerView connectivityListenerView,
                                        ILoadingView loadingView,
                                        IReportCommand reportCommand) {

        ReportPresenter presenter = new ReportPresenter(view, connectivityListenerView, loadingView);

        presenter.setReportCommand(reportCommand);

        return presenter;
    }

    @UserScope
    @Provides
    public IReportView getReportView() {
        return this.view;
    }

    @UserScope
    @Provides
    public IConnectivityListenerView getConnectivityListenerView() {
        return this.connectivityListenerView;
    }

    @UserScope
    @Provides
    public ILoadingView getLoadingView() {
        return this.loadingView;
    }

    @UserScope
    @Provides
    public IReportCommand getReportCommand(OkHttpClient client, Gson gson, IAppConfiguration appConfiguration) {
        return new ReportCommand(client, gson, appConfiguration);
    }
}
