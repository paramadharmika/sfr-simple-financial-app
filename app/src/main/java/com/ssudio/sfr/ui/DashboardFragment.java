package com.ssudio.sfr.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ssudio.sfr.R;
import com.ssudio.sfr.SFRApplication;

import com.ssudio.sfr.components.app.DaggerLocalStorageComponents;
/*import com.ssudio.sfr.components.ui.DaggerDashboardComponents;*/
import com.ssudio.sfr.components.ui.BasePresenterComponent;
import com.ssudio.sfr.components.ui.DaggerBasePresenterComponent;
import com.ssudio.sfr.components.ui.DashboardComponents;
import com.ssudio.sfr.dashboard.model.DashboardFeedModel;
import com.ssudio.sfr.dashboard.presenter.IDashboardPresenter;
import com.ssudio.sfr.dashboard.presenter.IDashboardView;
import com.ssudio.sfr.dashboard.ui.DashboardFeedDataAdapter;
import com.ssudio.sfr.modules.DashboardModule;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.NetworkModule;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements IDashboardView {
    @Inject
    protected IDashboardPresenter presenter;

    private DashboardComponents components;

    @BindView(R.id.swipeLayout)
    protected SwipeRefreshLayout swipeLayout;
    @BindView(R.id.lstFeed)
    protected ListView lstFeed;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ButterKnife.bind(this, v);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getDashboardContent();
            }
        });

        /*DashboardComponents components = DaggerDashboardComponents.builder()
                .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                .dashboardModule(new DashboardModule(this))
                .build();

        components.inject(this);*/

        BasePresenterComponent basePresenterComponent = DaggerBasePresenterComponent.builder()
                .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .localAuthenticationModule(new LocalAuthenticationModule())
                .networkModule(new NetworkModule())
                .build();

        components = basePresenterComponent.newDashboardSubComponent(new DashboardModule(this));

        components.inject(this);

        presenter.getDashboardContent();

        return v;
    }

    @Override
    public void bindContent(final ArrayList<DashboardFeedModel> result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);

                DashboardFeedDataAdapter adapter = new DashboardFeedDataAdapter(
                        getActivity().getBaseContext(), result);

                lstFeed.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDestroy() {
        presenter.unregisterEventHandler();

        super.onDestroy();
    }
}
