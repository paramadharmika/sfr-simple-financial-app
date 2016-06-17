package com.ssudio.sfr.report.presenter;

import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.report.command.IReportCommand;
import com.ssudio.sfr.report.command.IUploadReportCommand;
import com.ssudio.sfr.report.event.APIGetReportProgressEvent;
import com.ssudio.sfr.report.event.APIUploadReportProgressEvent;
import com.ssudio.sfr.report.event.ReportEvent;
import com.ssudio.sfr.report.event.UploadReportEvent;
import com.ssudio.sfr.report.model.ReportRequestModel;
import com.ssudio.sfr.report.model.UploadReportModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

public class ReportPresenter implements IReportPresenter {
    protected IReportCommand reportCommand;
    protected IUploadReportCommand uploadReportCommand;
    private final IReportView view;
    private final ILoadingView loadingView;
    private final IConnectivityListenerView connectivityListenerView;

    @Inject
    public ReportPresenter(IReportView view,
                           IConnectivityListenerView connectivityListenerView,
                           ILoadingView loadingView) {

        this.view = view;
        this.connectivityListenerView = connectivityListenerView;
        this.loadingView = loadingView;

        EventBus.getDefault().register(this);
    }

    @Override
    public void getReport(ReportRequestModel model) {
        reportCommand.executeAsync(model);
    }

    @Override
    public void uploadFile(UploadReportModel model) {
        uploadReportCommand.executeAsync(model);
    }

    @Subscribe
    public void onAPIGetReportProgressEvent(APIGetReportProgressEvent e) {
        loadingView.showLoading();
    }

    @Subscribe
    public void onReportEvent(ReportEvent e) {
        view.bindReport(e.getReports());
        loadingView.dismissLoading();
    }

    @Subscribe
    public void onAPIReportUploadStarted(APIUploadReportProgressEvent e) {
        loadingView.showLoading();
    }

    @Subscribe
    public void onReportUploadEvent(UploadReportEvent e) {
        view.showReportItemUploaded(e.getEventStatus() == UploadReportEvent.REPORT_UPLOADED, e.getMessage());
        loadingView.dismissLoading();
    }

    @Subscribe
    public void onNetworkEvent(NetworkConnectivityEvent e) {
        connectivityListenerView.showMessage(e);
    }

    @Override
    public void unregisterEventHandler() {
        EventBus.getDefault().unregister(this);
    }

    public void setReportCommand(IReportCommand reportCommand) {
        this.reportCommand = reportCommand;
    }

    public void setUploadCommand(IUploadReportCommand uploadReportCommand) {
        this.uploadReportCommand = uploadReportCommand;
    }
}
