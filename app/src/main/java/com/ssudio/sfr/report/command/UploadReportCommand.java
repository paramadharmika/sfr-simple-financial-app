package com.ssudio.sfr.report.command;

import com.google.gson.Gson;
import com.ssudio.sfr.configuration.IAppConfiguration;
import com.ssudio.sfr.report.command.retrofit.IUploadRetrofitCommand;
import com.ssudio.sfr.report.model.UploadReportModel;

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
        /*RequestBody title = RequestBody.create(MediaType.parse("text/plain"), "upload-report.jpeg");*/
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(param.getId()));

        Call<String> call = uploadCommand.uploadReport(image,  verificationCode, id);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String s = response.message();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return false;
    }
}
