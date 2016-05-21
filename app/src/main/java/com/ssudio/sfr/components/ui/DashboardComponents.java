package com.ssudio.sfr.components.ui;

import com.ssudio.sfr.dashboard.presenter.IDashboardPresenter;
import com.ssudio.sfr.modules.DashboardModule;
import com.ssudio.sfr.scope.UserScope;
import com.ssudio.sfr.ui.DashboardFragment;

import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {DashboardModule.class})
public interface DashboardComponents {
    IDashboardPresenter provideDashboardPresenter();

    void inject(DashboardFragment dashboardFragment);
}
