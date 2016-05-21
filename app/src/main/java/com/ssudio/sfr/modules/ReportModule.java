package com.ssudio.sfr.modules;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
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

    public ReportModule(IReportView view) {
        this.view = view;
    }

    @UserScope
    @Provides
    public IReportCommand getReportCommand(OkHttpClient client, Gson gson, IAppConfiguration appConfiguration) {
        return new ReportCommand(client, gson, appConfiguration);
    }

    @UserScope
    @Provides
    public IReportView getReportView() {
        return this.view;
    }

    @UserScope
    @Provides
    public ReportPresenter getPresenter(IReportView view, IReportCommand reportCommand) {
        ReportPresenter presenter = new ReportPresenter(view);

        presenter.setReportCommand(reportCommand);

        return presenter;
    }
}
