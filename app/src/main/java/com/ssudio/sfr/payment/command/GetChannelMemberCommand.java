package com.ssudio.sfr.payment.command;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.request.SFRGeneralPostParameter;
import com.ssudio.sfr.network.response.SFRApiGetResponse;
import com.ssudio.sfr.payment.event.APIPaymentProgressEvent;
import com.ssudio.sfr.payment.event.GetMemberPaymentMethodEvent;
import com.ssudio.sfr.payment.model.MemberPaymentModel;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetChannelMemberCommand implements IGetChannelMemberCommand {
    private IAppConfiguration appConfiguration;
    private OkHttpClient client;
    private Gson gson;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public GetChannelMemberCommand(OkHttpClient client, Gson gson, IAppConfiguration appConfiguration) {
        this.client = client;
        this.gson = gson;
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void executeAsync(SFRGeneralPostParameter param) {
        RequestBody body = RequestBody.create(JSON, gson.toJson(param));

        Request request = new Request.Builder()
                .url(appConfiguration.getBaseUrl() + "getChannelMember")
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

                SFRApiGetResponse<MemberPaymentModel> models = gson.fromJson(result,
                        new TypeToken<SFRApiGetResponse<MemberPaymentModel>>(){}.getType());

                GetMemberPaymentMethodEvent event = new GetMemberPaymentMethodEvent(models.isSuccess(),
                        models.getStatus(), models.getData());

                //EventBus.getDefault().post(new APIPaymentProgressEvent(false));
                EventBus.getDefault().post(event);
            }
        });

        //EventBus.getDefault().post(new APIPaymentProgressEvent(true));
    }
}
