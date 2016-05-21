package com.ssudio.sfr.components.ui;

import com.ssudio.sfr.modules.ReportModule;
import com.ssudio.sfr.modules.SFRApplicationModule;
import com.ssudio.sfr.report.presenter.ReportPresenter;
import com.ssudio.sfr.scope.UserScope;
import com.ssudio.sfr.ui.ReportFragment;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {ReportModule.class})
public interface ReportComponents {
    ReportPresenter provideReportPresenter();

    void inject(ReportFragment reportFragment);
}
