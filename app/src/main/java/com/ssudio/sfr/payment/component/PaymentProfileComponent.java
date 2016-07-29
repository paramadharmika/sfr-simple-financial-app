package com.ssudio.sfr.payment.component;

import com.ssudio.sfr.PaymentProfileActivity;
import com.ssudio.sfr.payment.module.PaymentCommandModule;
import com.ssudio.sfr.payment.module.PaymentProfileModule;
import com.ssudio.sfr.payment.presenter.PaymentProfilePresenter;
import com.ssudio.sfr.payment.ui.PaymentProfileFragment;
import com.ssudio.sfr.scope.UserScope;

import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {PaymentProfileModule.class, PaymentCommandModule.class})
public interface PaymentProfileComponent {
    PaymentProfilePresenter providePaymentPresenter();

    void inject(PaymentProfileFragment paymentProfileFragment);
}
