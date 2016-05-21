package com.ssudio.sfr.report.presenter;

import com.ssudio.sfr.report.command.IReportCommand;
import com.ssudio.sfr.report.event.ReportEvent;
import com.ssudio.sfr.report.model.ReportRequestModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

public class ReportPresenter implements IReportPresenter {
    protected IReportCommand reportCommand;
    private IReportView view;

    @Inject
    public ReportPresenter(IReportView view) {
        this.view = view;

        EventBus.getDefault().register(this);
    }

    @Override
    public void getReport(ReportRequestModel model) {
        reportCommand.executeAsync(model);
    }

    @Subscribe
    public void onReportEvent(ReportEvent e) {
        view.bindReport(e.getReports());
    }

    @Override
    public void unregisterEventHandler() {
        EventBus.getDefault().unregister(this);
    }

    public void setReportCommand(IReportCommand reportCommand) {
        this.reportCommand = reportCommand;
    }
}
