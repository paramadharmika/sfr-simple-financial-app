package com.ssudio.sfr.registration.command;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.registration.event.APISaveProfileProgressEvent;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.request.SFRNetworkRequestType;
import com.ssudio.sfr.network.response.SFRApiPostResponse;
import com.ssudio.sfr.registration.event.ProfileEvent;
import com.ssudio.sfr.registration.model.UserModel;

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

public class UpdateProfileCommand implements IUpdateProfileCommand {
    private IAppConfiguration appConfiguration;
    private OkHttpClient client;
    private Gson gson;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Inject
    public UpdateProfileCommand(OkHttpClient client, Gson gson, IAppConfiguration appConfiguration) {
        this.client = client;
        this.gson = gson;
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void executeAsync(final UserModel param) {
        RequestBody body = RequestBody.create(JSON, gson.toJson(param));

        Request request = new Request.Builder()
                .url(appConfiguration.getBaseUrl() + "updateUser")
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

                apiResponse.setOperation(SFRNetworkRequestType.Update);

                ProfileEvent profileEvent = new ProfileEvent(
                        apiResponse.isSuccess(),
                        apiResponse.getStatus(),
                        ProfileEvent.UPDATED,
                        param);

                EventBus.getDefault().post(profileEvent);
            }
        });

        EventBus.getDefault().post(new APISaveProfileProgressEvent(true));
    }
}
