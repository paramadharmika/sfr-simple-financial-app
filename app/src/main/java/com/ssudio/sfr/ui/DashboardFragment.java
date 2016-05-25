package com.ssudio.sfr.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ssudio.sfr.R;
import com.ssudio.sfr.SFRApplication;

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
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment
        implements IDashboardView, IConnectivityListenerView, ILoadingView {

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

        BasePresenterComponent basePresenterComponent = DaggerBasePresenterComponent.builder()
                .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .localAuthenticationModule(new LocalAuthenticationModule())
                .networkModule(new NetworkModule())
                .build();

        components = basePresenterComponent.newDashboardSubComponent(new DashboardModule(this, this, this));

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

                dismissLoading();
            }
        });
    }

    @Override
    public void showMessage(final NetworkConnectivityEvent e) {
        final Activity activity = getActivity();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message;

                if (e.isConnected()) {
                    message = getString(R.string.message_network_connected);
                } else {
                    message = getString(R.string.message_network_is_not_connected);
                }

                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showLoading() {
        getParentView().showLoading();
    }

    @Override
    public void dismissLoading() {
        getParentView().dismissLoading();
    }

    @Override
    public IContainerViewCallback getParentView() {
        return (IContainerViewCallback)getActivity();
    }

    @Override
    public void onDestroy() {
        presenter.unregisterEventHandler();

        super.onDestroy();
    }
}
