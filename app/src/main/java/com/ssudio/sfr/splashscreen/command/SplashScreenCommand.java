package com.ssudio.sfr.splashscreen.command;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.network.response.SFRApiGetResponse;
import com.ssudio.sfr.splashscreen.event.SplashScreenEvent;
import com.ssudio.sfr.splashscreen.model.SplashScreenModel;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashScreenCommand implements ISplashScreenCommand {
    private IAppConfiguration appConfiguration;
    private OkHttpClient client;
    private Gson gson;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public SplashScreenCommand(OkHttpClient client, Gson gson, IAppConfiguration appConfiguration) {
        this.client = client;
        this.gson = gson;
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void executeAsync(Void param) {
        Request request = new Request.Builder()
                .url(appConfiguration.getBaseUrl() + "getAppInfo")
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //todo: post network io exception to main view
//                if (isNewUser) {
//                    EventBus.getDefault().post(new RegistrationEvent(false, e.getMessage(), null));
//                } else {
//                    EventBus.getDefault().post(new UpdateRegistrationEvent(false, e.getMessage(), null));
//                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                SFRApiGetResponse<SplashScreenModel> apiResponse = gson.fromJson(result, new TypeToken<SFRApiGetResponse<SplashScreenModel>>(){}.getType());

                if (apiResponse.getData().size() > 0) {
                    SplashScreenModel model = apiResponse.getData().get(0);

                    SplashScreenEvent event = new SplashScreenEvent(model);

                    EventBus.getDefault().post(event);
                } else {
                    //todo: post no data event
                }
            }
        });
    }
}
