package com.ssudio.sfr.registration.command;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.network.request.SFRGeneralPostParameter;
import com.ssudio.sfr.network.response.SFRApiGetResponse;
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

public class GetProfileCommand implements IGetProfileCommand {
    private boolean isNewUser;
    private IAppConfiguration appConfiguration;
    private OkHttpClient client;
    private Gson gson;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Inject
    public GetProfileCommand(OkHttpClient client, Gson gson, IAppConfiguration appConfiguration) {
        this.client = client;
        this.gson = gson;
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void executeAsync(final SFRGeneralPostParameter param) {
        RequestBody body = RequestBody.create(JSON, gson.toJson(param));

        Request request = new Request.Builder()
                .url(appConfiguration.getBaseUrl() + "getUser")
                .post(body)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //todo: post io network exception
//                ProfileEvent profileEvent = new ProfileEvent(false,
//                        e.getMessage(),
//                        ProfileEvent.IO_NETWORK_EXCEPTION,
//                        null);
//
//                EventBus.getDefault().post(profileEvent);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                SFRApiGetResponse<UserModel> apiResponse = gson.fromJson(result,
                        new TypeToken<SFRApiGetResponse<UserModel>>(){}.getType());

                UserModel model = apiResponse.getData().get(0);

                ProfileEvent profileEvent = new ProfileEvent(apiResponse.isSuccess(),
                        apiResponse.getStatus(),
                        ProfileEvent.LOADED,
                        model);

                EventBus.getDefault().post(profileEvent);
            }
        });
    }
}
