package com.ssudio.sfr.payment.ui;

import android.content.res.Configuration;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ssudio.sfr.R;
import com.ssudio.sfr.SFRApplication;
import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.components.ui.BasePresenterComponent;
import com.ssudio.sfr.components.ui.DaggerBasePresenterComponent;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.NetworkModule;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.payment.component.PaymentComponent;
import com.ssudio.sfr.payment.model.PaymentModel;
import com.ssudio.sfr.payment.model.PreferredPaymentModel;
import com.ssudio.sfr.payment.module.PaymentModule;
import com.ssudio.sfr.payment.presenter.PaymentPresenter;
import com.ssudio.sfr.registration.model.UserModel;
import com.ssudio.sfr.ui.IContainerViewCallback;
import com.ssudio.sfr.utility.MoneyTextWatcher;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment
        implements IPaymentView, IConnectivityListenerView, ILoadingView {

    @Inject
    LocalAuthenticationService localAuthService;
    @Inject
    protected PaymentPresenter paymentPresenter;
    private PaymentComponent components;

    @BindView(R.id.spinnerChannelPayment)
    protected Spinner spinnerChannelPayment;
    @BindView(R.id.lblMinPayment)
    protected TextView lblMinPayment;
    @BindView(R.id.txtAmount)
    protected EditText txtAmount;

    private ArrayList<PaymentModel> payments;
    private PaymentModel selectedPaymentModel;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        selectedPaymentModel = new PaymentModel();

        BasePresenterComponent basePresenterComponent =
                DaggerBasePresenterComponent.builder()
                    .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                    .localStorageModule(new LocalStorageModule())
                    .localAuthenticationModule(new LocalAuthenticationModule())
                    .networkModule(new NetworkModule())
                    .build();

        components = basePresenterComponent.newPaymentSubComponent(new PaymentModule(this, this, this));

        components.inject(this);

        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        ButterKnife.bind(this, v);

        setupViews();

        return v;
    }

    private void setupViews() {
        if (localAuthService.isValidUser()) {
            paymentPresenter.getPayments();
        }

        txtAmount.setRawInputType(Configuration.KEYBOARD_12KEY);
        txtAmount.addTextChangedListener(new MoneyTextWatcher(txtAmount));
    }

    @Override
    public void bindPayments(final ArrayList<PaymentModel> paymentsModel) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                payments = paymentsModel;

                ArrayAdapter<String> spinnerArrayAdapter = getPaymentChannelSpinnerDataSource();

                spinnerChannelPayment.setAdapter(spinnerArrayAdapter);

                spinnerChannelPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedPaymentModel = payments.get(position);

                        lblMinPayment.setText(String.format("%s %s",
                                getString(R.string.min_payment),
                                selectedPaymentModel.getMinPayment()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
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

    @OnClick(R.id.btnSubmit)
    protected void btnSubmit_Click() {
        clearErrorMessageOnEditText();

        double currency = Double.parseDouble(txtAmount.getText().toString().replace(".", ""));

        if (currency < Double.parseDouble(selectedPaymentModel.getMinPayment())) {
            txtAmount.setError(getString(R.string.min_payment_error_message)
                    + selectedPaymentModel.getMinPayment());

            return;
        }

        paymentPresenter.savePayment(selectedPaymentModel.getIdChannel(), currency);
    }

    private void clearErrorMessageOnEditText() {
        txtAmount.setError(null);
    }

    @Override
    public void showMessage(boolean isSuccess, String message) {
        dismissLoading();

        getParentView().showMessage(isSuccess, message);
    }

    @Override
    public void showMessage(final NetworkConnectivityEvent e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message;

                if (e.isConnected()) {
                    message = getString(R.string.message_network_connected);
                } else {
                    message = getString(R.string.message_network_is_not_connected);
                }

                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
        return (IContainerViewCallback) getActivity();
    }

    @Override
    public void onDestroy() {
        paymentPresenter.unregisterEventHandler();

        super.onDestroy();
    }
}
