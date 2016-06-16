package com.ssudio.sfr.report.command.retrofit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IUploadRetrofitCommand {
    @Multipart
    @POST("uploadReport")
    Call<String> uploadReport(
                      @Part("userPhoto\"; filename=\"uploadReport.jpeg") RequestBody file,
                      @Part(value = "verification_code", encoding = "utf-8") RequestBody verification_code,
                      @Part(value = "id", encoding = "utf-8") RequestBody id);
}
