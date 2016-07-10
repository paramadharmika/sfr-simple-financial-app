package com.ssudio.sfr.payment.component;

import com.ssudio.sfr.payment.module.PaymentModule;
import com.ssudio.sfr.payment.presenter.PaymentPresenter;
import com.ssudio.sfr.payment.ui.PaymentFragment;
import com.ssudio.sfr.scope.UserScope;

import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {PaymentModule.class})
public interface PaymentComponent {
    PaymentPresenter providePaymentPresenter();

    void inject(PaymentFragment paymentFragment);
}
