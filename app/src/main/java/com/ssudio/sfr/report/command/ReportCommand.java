package com.ssudio.sfr.report.command;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.dashboard.events.DashboardEvent;
import com.ssudio.sfr.dashboard.model.DashboardFeedModel;
import com.ssudio.sfr.network.response.SFRApiGetResponse;
import com.ssudio.sfr.report.event.ReportEvent;
import com.ssudio.sfr.report.model.ReportRequestModel;
import com.ssudio.sfr.report.model.ReportResponseModel;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReportCommand implements IReportCommand {
    private IAppConfiguration appConfiguration;
    private OkHttpClient client;
    private Gson gson;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public ReportCommand(OkHttpClient client, Gson gson, IAppConfiguration appConfiguration) {
        this.client = client;
        this.gson = gson;
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void executeAsync(ReportRequestModel param) {
        RequestBody body = RequestBody.create(JSON, gson.toJson(param));

        Request request = new Request.Builder()
                .url(appConfiguration.getBaseUrl() + "getReport")
                .post(body)
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

                SFRApiGetResponse<ReportResponseModel> apiResponse = gson.fromJson(result,
                        new TypeToken<SFRApiGetResponse<ArrayList<ReportResponseModel>>>(){}.getType());

                ReportEvent event;

                if (apiResponse.isSuccess()) {
                    event = new ReportEvent(ReportEvent.REPORT_GENERATED, apiResponse.getData());
                } else {
                    event = new ReportEvent(ReportEvent.FAILED_TO_GENERATE_REPORT, apiResponse.getData());
                }

                EventBus.getDefault().post(event);
            }
        });
    }
}
