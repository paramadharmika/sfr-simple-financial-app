package com.ssudio.sfr;

import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.ssudio.sfr.payment.ui.PaymentProfileFragment;
import com.ssudio.sfr.ui.IContainerViewCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentProfileActivity extends AppCompatActivity implements IContainerViewCallback {

    @BindView(R.id.coordinatorLayout)
    protected CoordinatorLayout coordinatorLayout;
    private KProgressHUD loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_profile);

        ButterKnife.bind(this);

        setFragmentInitializingValue();
    }

    private void setFragmentInitializingValue() {
        FragmentManager fm = getFragmentManager();

        PaymentProfileFragment paymentProfileFragment =
                (PaymentProfileFragment) fm.findFragmentById(R.id.paymentProfileFragment);

        paymentProfileFragment.setInitializePaymentProfile(true);
    }

    /*@Override
    public void showMessage(NetworkConnectivityEvent e) {
        if (!e.isConnected()) {
            showMessage(false, getResources().getString(R.string.message_network_is_not_connected));
        } else {
            showMessage(true, getResources().getString(R.string.message_network_connected));
        }
    }*/

    @Override
    public void showLoading() {
        loadingView = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        loadingView.show();
    }

    @Override
    public void dismissLoading() {
        if (loadingView != null) {
            loadingView.dismiss();
        }
    }

    @Override
    public void showMessage(boolean isSuccess, String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo: request focus to text maybe?
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
