package com.ssudio.sfr.dashboard.command;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.dashboard.events.DashboardEvent;
import com.ssudio.sfr.dashboard.model.DashboardFeedModel;
import com.ssudio.sfr.dashboard.events.APIDashboardCallProgressEvent;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.response.SFRApiGetResponse;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DashboardCommand implements IDashboardCommand {
    private IAppConfiguration appConfiguration;
    private OkHttpClient client;
    private Gson gson;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public DashboardCommand(OkHttpClient client, Gson gson, IAppConfiguration appConfiguration) {
        this.client = client;
        this.gson = gson;
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void executeAsync(String param) {
        Request request = new Request.Builder()
                .url(appConfiguration.getBaseUrl() + "getFeed")
                .get()
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

                SFRApiGetResponse<DashboardFeedModel> apiResponse = gson.fromJson(result,
                        new TypeToken<SFRApiGetResponse<DashboardFeedModel>>(){}.getType());

                DashboardEvent event = new DashboardEvent(DashboardEvent.INITIAL_LOAD, apiResponse.getData());

                EventBus.getDefault().post(event);
            }
        });

        EventBus.getDefault().post(new APIDashboardCallProgressEvent(true));
    }
}
