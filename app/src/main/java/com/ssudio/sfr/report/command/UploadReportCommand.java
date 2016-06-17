package com.ssudio.sfr.report.command;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.dashboard.events.APIDashboardCallProgressEvent;
import com.ssudio.sfr.network.request.SFRNetworkRequestType;
import com.ssudio.sfr.network.response.SFRApiPostResponse;
import com.ssudio.sfr.report.command.retrofit.IUploadRetrofitCommand;
import com.ssudio.sfr.report.event.APIUploadReportProgressEvent;
import com.ssudio.sfr.report.event.UploadReportEvent;
import com.ssudio.sfr.report.model.UploadReportModel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadReportCommand implements IUploadReportCommand {
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    private IAppConfiguration appConfiguration;
    private OkHttpClient client;
    private Gson gson;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public UploadReportCommand(OkHttpClient client, Gson gson, IAppConfiguration appConfiguration) {
        this.client = client;
        this.gson = gson;
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void executeAsync(final UploadReportModel param) {
        uploadFile(param);
    }

    private boolean uploadFile(UploadReportModel param) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(appConfiguration.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IUploadRetrofitCommand uploadCommand = retrofit.create(IUploadRetrofitCommand.class);

        RequestBody image = RequestBody.create(MediaType.parse("image/jpeg"), new File(param.getFileName()));
        RequestBody verificationCode = RequestBody.create(MediaType.parse("text/plain"), param.getVerificationCode());
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(param.getId()));

        EventBus.getDefault().post(new APIUploadReportProgressEvent(true));

        Call<SFRApiPostResponse> call = uploadCommand.uploadReport(image,  verificationCode, id);

        call.enqueue(new Callback<SFRApiPostResponse>() {
            @Override
            public void onResponse(Call<SFRApiPostResponse> call, retrofit2.Response<SFRApiPostResponse> response) {
                SFRApiPostResponse postResponse = response.body();

                postResponse.setOperation(SFRNetworkRequestType.Insert);

                EventBus.getDefault().post(
                        new UploadReportEvent(
                                postResponse.isSuccess() ? UploadReportEvent.REPORT_UPLOADED : UploadReportEvent.FAILED_TO_UPLOAD_REPORT,
                                postResponse.getStatus()));
            }

            @Override
            public void onFailure(Call<SFRApiPostResponse> call, Throwable t) {
                t.printStackTrace();

                EventBus.getDefault().post(
                        new UploadReportEvent(
                                UploadReportEvent.FAILED_TO_UPLOAD_REPORT,
                                "Unknown error has occurred"));
            }
        });

        return false;
    }
}
