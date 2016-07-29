package com.ssudio.sfr.payment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.ssudio.sfr.MainActivity;
import com.ssudio.sfr.R;
import com.ssudio.sfr.SFRApplication;
import com.ssudio.sfr.components.ui.BasePresenterComponent;
import com.ssudio.sfr.components.ui.DaggerBasePresenterComponent;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.NetworkModule;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.payment.component.PaymentProfileComponent;
import com.ssudio.sfr.payment.model.PaymentModel;
import com.ssudio.sfr.payment.module.PaymentProfileModule;
import com.ssudio.sfr.payment.presenter.PaymentProfilePresenter;
import com.ssudio.sfr.ui.IContainerViewCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentProfileFragment extends Fragment implements IPaymentProfileView, IConnectivityListenerView, ILoadingView {

    @BindView(R.id.spinnerChannelPayment)
    protected Spinner spinnerChannelPayment;
    @BindView(R.id.txtAccountName)
    protected EditText txtAccountName;
    @BindView(R.id.txtAccountNumber)
    protected EditText txtAccountNumber;

    private PaymentProfileComponent components;
    @Inject
    protected PaymentProfilePresenter paymentProfilePresenter;

    private ArrayList<PaymentModel> payments;
    private PaymentModel selectedPaymentModel;
    private KProgressHUD loadingView;

    public PaymentProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_profile, container, false);

        ButterKnife.bind(this, view);

        setupDependencies();

        paymentProfilePresenter.getPayments();

        return view;
    }

    private void setupDependencies() {
        BasePresenterComponent basePresenterComponent = DaggerBasePresenterComponent.builder()
                .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .networkModule(new NetworkModule())
                .build();

        components = basePresenterComponent.newPaymentProfileSubComponent(new PaymentProfileModule(this, this, this));

        components.inject(this);
    }

    @Override
    public void showMessage(NetworkConnectivityEvent e) {
        if (!e.isConnected()) {
            getParentView().showMessage(false, getResources().getString(R.string.message_network_is_not_connected));
        }
    }

    @Override
    public void showMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));

        getActivity().finish();
    }

    @Override
    public void showMessage(boolean isSuccess, String message) {
        getParentView().showMessage(isSuccess, message);
    }

    @Override
    public void bindPayments(final ArrayList<PaymentModel> paymentsModel) {
        payments = paymentsModel;

        ArrayAdapter<String> spinnerArrayAdapter = getPaymentChannelSpinnerDataSource();

        spinnerChannelPayment.setAdapter(spinnerArrayAdapter);

        spinnerChannelPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentModel = payments.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        paymentProfilePresenter.getMemberPaymentMethods();
    }

    @NonNull
    private ArrayAdapter<String> getPaymentChannelSpinnerDataSource() {
        ArrayList<String> result = new ArrayList<>();

        for (PaymentModel pm : payments) {
            result.add(pm.getChannelName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, result);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return spinnerArrayAdapter;
    }

    @Override
    public IContainerViewCallback getParentView() {
        return (IContainerViewCallback) getActivity();
    }

    @Override
    public void showLoading() {
        loadingView = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        loadingView.show();
    }

    @Override
    public void dismissLoading() {
        if (loadingView.isShowing()) {
            loadingView.dismiss();
        }
    }

    @OnClick(R.id.btnSubmit)
    protected void btnSubmit_Click() {
        clearErrorMessageOnEditText();

        if (selectedPaymentModel == null) {
            return;
        }

        paymentProfilePresenter.savePaymentProfile(selectedPaymentModel.getIdChannel(),
                0,
                txtAccountName.getText().toString(),
                txtAccountNumber.getText().toString());
    }

    private void clearErrorMessageOnEditText() {
        txtAccountName.setError(null);
        txtAccountNumber.setError(null);
    }

    @Override
    public void onDestroy() {
        paymentProfilePresenter.unregisterEventHandler();

        super.onDestroy();
    }

    public void setInitializePaymentProfile(boolean value) {
        paymentProfilePresenter.setInitializingPaymentProfile(value);
    }
}
