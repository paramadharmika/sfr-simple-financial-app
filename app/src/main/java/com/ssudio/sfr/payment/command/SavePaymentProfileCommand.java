package com.ssudio.sfr.payment.command;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.request.SFRNetworkRequestType;
import com.ssudio.sfr.network.response.SFRApiPostResponse;
import com.ssudio.sfr.payment.event.APIPaymentProgressEvent;
import com.ssudio.sfr.payment.event.SavePaymentEvent;
import com.ssudio.sfr.payment.model.MemberPaymentModel;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SavePaymentProfileCommand implements ISavePaymentProfileCommand {
    private IAppConfiguration appConfiguration;
    private OkHttpClient client;
    private Gson gson;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Inject
    public SavePaymentProfileCommand(OkHttpClient client, Gson gson, IAppConfiguration appConfiguration) {
        this.client = client;
        this.gson = gson;
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void executeAsync(final MemberPaymentModel param) {
        RequestBody body = RequestBody.create(JSON, gson.toJson(param));

        Request request = new Request.Builder()
                .url(appConfiguration.getBaseUrl() + "saveChannelMember")
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EventBus.getDefault().post(new NetworkConnectivityEvent(false));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                SFRApiPostResponse apiResponse = gson.fromJson(result, SFRApiPostResponse.class);

                if (param.getIdChannelMember() == 0) {
                    apiResponse.setOperation(SFRNetworkRequestType.Insert);
                } else {
                    apiResponse.setOperation(SFRNetworkRequestType.Update);
                }

                SavePaymentEvent event = new SavePaymentEvent(
                        apiResponse.isSuccess(),
                        apiResponse.getStatus());

                EventBus.getDefault().post(new APIPaymentProgressEvent(false));
                EventBus.getDefault().post(event);
            }
        });

        EventBus.getDefault().post(new APIPaymentProgressEvent(true));
    }
}
