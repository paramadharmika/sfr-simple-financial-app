package com.ssudio.sfr.payment.component;

import com.ssudio.sfr.payment.module.PaymentCommandModule;
import com.ssudio.sfr.payment.module.PaymentRequestModule;
import com.ssudio.sfr.payment.presenter.PaymentRequestPresenter;
import com.ssudio.sfr.payment.ui.PaymentRequestFragment;
import com.ssudio.sfr.scope.UserScope;

import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {PaymentRequestModule.class, PaymentCommandModule.class})
public interface PaymentRequestComponent {
    PaymentRequestPresenter providePaymentPresenter();

    void inject(PaymentRequestFragment paymentFragment);
}
