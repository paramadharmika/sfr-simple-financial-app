package com.ssudio.sfr.report.command.retrofit;

import com.ssudio.sfr.network.response.SFRApiPostResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IUploadRetrofitCommand {
    @Multipart
    @POST("uploadReport")
    Call<SFRApiPostResponse> uploadReport(
                      @Part("fileUpload1\"; filename=\"uploadReport.jpeg\"") RequestBody file,
                      @Part("verification_code") RequestBody verification_code,
                      @Part("id") RequestBody id);
}
