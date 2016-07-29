package com.ssudio.sfr.payment.module;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.payment.command.GetChannelMemberCommand;
import com.ssudio.sfr.payment.command.GetPaymentChannelCommand;
import com.ssudio.sfr.payment.command.IGetChannelMemberCommand;
import com.ssudio.sfr.payment.command.IGetPaymentChannelCommand;
import com.ssudio.sfr.payment.command.ISavePaymentChannelCommand;
import com.ssudio.sfr.payment.command.SavePaymentChannelCommand;
import com.ssudio.sfr.scope.UserScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class PaymentCommandModule {
    @Provides
    @UserScope
    public IGetPaymentChannelCommand getPaymentChannelCommand(OkHttpClient client, Gson gson,
                                                              IAppConfiguration appConfiguration) {
        return new GetPaymentChannelCommand(client, gson, appConfiguration);
    }

    @Provides
    @UserScope
    public IGetChannelMemberCommand getChannelMemberCommand(OkHttpClient client, Gson gson,
                                                            IAppConfiguration appConfiguration) {
        return new GetChannelMemberCommand(client, gson, appConfiguration);
    }

    @Provides
    @UserScope
    public ISavePaymentChannelCommand getSavePaymentCommand(OkHttpClient client, Gson gson,
                                                            IAppConfiguration appConfiguration) {
        return new SavePaymentChannelCommand(client, gson, appConfiguration);
    }
}
